package com.xiaoyua.service;

import com.xiaoyua.vo.file.FileVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务接口
 * 
 * @author xiaoyu
 */
public interface FileService {
    
    /**
     * 上传文件
     * 
     * @param file 文件
     * @param bizType 业务类型
     * @param userId 用户ID
     * @return 文件信息
     */
    FileVO uploadFile(MultipartFile file, String bizType, Long userId);
    
    /**
     * 根据ID获取文件信息
     * 
     * @param fileId 文件ID
     * @return 文件信息
     */
    FileVO getFileById(Long fileId);
    
    /**
     * 删除文件
     * 
     * @param fileId 文件ID
     * @param userId 用户ID（用于权限验证）
     * @return 是否删除成功
     */
    boolean deleteFile(Long fileId, Long userId);

}
