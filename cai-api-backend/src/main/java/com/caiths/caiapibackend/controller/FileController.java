package com.caiths.caiapibackend.controller;

import cn.hutool.core.io.FileUtil;
import com.caiths.caiapibackend.common.BaseResponse;
import com.caiths.caiapibackend.common.ErrorCode;
import com.caiths.caiapibackend.common.ResultUtils;
import com.caiths.caiapibackend.config.CosClientConfig;
import com.caiths.caiapibackend.manager.CosManager;
import com.caiths.caiapibackend.model.enums.FileUploadBizEnum;
import com.caiths.caiapibackend.model.enums.ImageStatusEnum;
import com.caiths.caiapibackend.model.file.UploadFileRequest;
import com.caiths.caiapibackend.model.vo.ImageVo;
import com.caiths.caiapibackend.model.vo.UserVO;
import com.caiths.caiapibackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Arrays;

/**
 * 文件控制器，提供文件上传相关接口。
 * <p>
 * 主要处理用户头像、文件上传等操作。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    private static final long ONE_M = 2 * 1024 * 1024L;

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    @Resource
    private CosClientConfig cosClientConfig;

    /**
     * 上传文件接口
     * <p>
     * 根据业务类型和用户信息，将文件上传到 COS，并返回访问链接。
     * </p>
     *
     * @param multipartFile     上传的文件
     * @param uploadFileRequest 上传文件请求信息，包含业务类型
     * @param request           HTTP 请求信息
     * @return 文件上传结果，包含文件的访问地址和状态
     */
    @PostMapping("/upload")
    public BaseResponse<ImageVo> uploadFile(@RequestPart("file") MultipartFile multipartFile,
                                            UploadFileRequest uploadFileRequest,
                                            HttpServletRequest request) {
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        ImageVo imageVo = new ImageVo();

        if (fileUploadBizEnum == null) {
            return uploadError(imageVo, multipartFile, "上传失败，请重试。");
        }

        String validationResult = validFile(multipartFile, fileUploadBizEnum);
        if (!"success".equals(validationResult)) {
            return uploadError(imageVo, multipartFile, validationResult);
        }

        UserVO loginUser = userService.getLoginUser(request);

        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), loginUser.getId(), filename);
        File tempFile = null;

        try {
            tempFile = File.createTempFile(filepath, null);
            multipartFile.transferTo(tempFile);
            cosManager.putObject(filepath, tempFile);

            imageVo.setName(multipartFile.getOriginalFilename());
            imageVo.setUid(RandomStringUtils.randomAlphanumeric(8));
            imageVo.setStatus(ImageStatusEnum.SUCCESS.getValue());
            imageVo.setUrl(cosClientConfig.getCosHost() + filepath);

            return ResultUtils.success(imageVo);
        } catch (Exception e) {
            log.error("File upload error, filepath = {}", filepath, e);
            return uploadError(imageVo, multipartFile, "上传失败，请重试。");
        } finally {
            if (tempFile != null && !tempFile.delete()) {
                log.error("File deletion failed, filepath = {}", filepath);
            }
        }
    }

    /**
     * 构建文件上传错误响应。
     *
     * @param imageVo       文件信息对象
     * @param multipartFile 上传的文件
     * @param message       错误信息
     * @return 错误响应对象
     */
    private BaseResponse<ImageVo> uploadError(ImageVo imageVo, MultipartFile multipartFile, String message) {
        imageVo.setName(multipartFile.getOriginalFilename());
        imageVo.setUid(RandomStringUtils.randomAlphanumeric(8));
        imageVo.setStatus(ImageStatusEnum.ERROR.getValue());
        return ResultUtils.error(imageVo, ErrorCode.OPERATION_ERROR, message);
    }

    /**
     * 校验文件的合法性。
     * <p>
     * 根据文件大小、类型等规则，验证文件是否符合业务需求。
     * </p>
     *
     * @param multipartFile     上传的文件
     * @param fileUploadBizEnum 业务类型枚举
     * @return 校验结果。如果校验通过，返回 "success"；否则返回错误信息。
     */
    private String validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        long fileSize = multipartFile.getSize();
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());

        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                return "文件大小不能超过 1M";
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp", "jfif").contains(fileSuffix)) {
                return "文件类型错误";
            }
        }
        return "success";
    }
}
