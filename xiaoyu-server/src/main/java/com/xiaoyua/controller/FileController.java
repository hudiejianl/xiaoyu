package com.xiaoyua.controller;

import com.xiaoyua.context.BaseContext;
import com.xiaoyua.result.Result;
import com.xiaoyua.service.FileService;
import com.xiaoyua.vo.file.FileVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器
 * 
 * @author xiaoyu
 */
@RestController
@RequestMapping("/files")
@Slf4j
@Tag(name = "文件管理", description = "文件上传、下载、删除等操作")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传文件至OSS，并记录文件信息")
    public Result<FileVO> upload(
            @Parameter(description = "要上传的文件", required = true)
            @RequestParam("file") MultipartFile file,
            
            @Parameter(description = "业务类型：AVATAR/BG/POST/TASK/COMMENT", required = true)
            @RequestParam("biz_type") 
            @NotBlank(message = "业务类型不能为空")
            @Pattern(regexp = "^(AVATAR|BG|POST|TASK|COMMENT)$", message = "业务类型必须是：AVATAR/BG/POST/TASK/COMMENT")
            String bizType) {
        
        log.info("开始上传文件，业务类型: {}, 文件名: {}", bizType, file.getOriginalFilename());
        
        // TODO: 从JWT token中获取用户ID，这里暂时使用固定值
        Long userId = BaseContext.getCurrentId();
        
        FileVO fileVO = fileService.uploadFile(file, bizType, userId);
        
        return Result.success("上传成功", fileVO);
    }
    
    @GetMapping("/{fileId}")
    @Operation(summary = "获取文件信息", description = "根据文件ID获取文件详细信息")
    public Result<FileVO> getFile(
            @Parameter(description = "文件ID", required = true)
            @PathVariable Long fileId) {
        
        FileVO fileVO = fileService.getFileById(fileId);
        return Result.success(fileVO);
    }
    
    @DeleteMapping("/{fileId}")
    @Operation(summary = "删除文件", description = "删除指定的文件（仅文件所有者可删除）")
    public Result<String> deleteFile(
            @Parameter(description = "文件ID", required = true)
            @PathVariable Long fileId) {
        
        log.info("开始删除文件，文件ID: {}", fileId);
        
        // TODO: 从JWT token中获取用户ID，这里暂时使用固定值
        Long userId = BaseContext.getCurrentId();
        
        boolean success = fileService.deleteFile(fileId, userId);
        
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

}
