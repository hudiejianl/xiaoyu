package com.xiaoyua.service.impl;

import com.xiaoyua.entity.FilePO;
import com.xiaoyua.mapper.FileMapper;
import com.xiaoyua.service.FileService;
import com.xiaoyua.utils.OssUtil;
import com.xiaoyua.vo.file.FileVO;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

/**
 * 文件服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    
    private final FileMapper fileMapper;
    
    
    private final OssUtil ossUtil;
    
    // 允许的文件类型
    private static final String[] ALLOWED_TYPES = {"jpg", "jpeg", "png", "gif", "bmp", "webp", "mp4", "avi", "mov", "mp3", "wav", "pdf", "doc", "docx", "txt"};
    
    // 最大文件大小 10MB
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024L;
    
    @Override
    public FileVO uploadFile(MultipartFile file, String bizType, Long userId) {
        log.info("开始上传文件，用户ID: {}, 业务类型: {}, 文件名: {}", userId, bizType, file.getOriginalFilename());
        
        // 验证文件
        validateFile(file);
        
        String objectName = null;
        String fileUrl = null;
        try {
            // 解析并校验业务类型
            FilePO.BizType bizTypeEnum = parseBizType(bizType);
            // 生成对象名称
            objectName = generateObjectName(file.getOriginalFilename(), bizTypeEnum.name());
            // 上传文件到OSS
            fileUrl = ossUtil.upload(file.getBytes(), objectName);
            // 生成缩略图URL（如果是图片）
            String thumbUrl = generateThumbnailUrl(fileUrl);
            // 保存文件信息到数据库
            FilePO filePO = new FilePO();
            filePO.setUserId(userId);
            filePO.setBizType(bizTypeEnum);
            filePO.setFileUrl(fileUrl);
            filePO.setThumbUrl(thumbUrl);
            filePO.setSize((int) file.getSize());
            filePO.setCreatedAt(LocalDateTime.now());

            fileMapper.insert(filePO);

            log.info("文件上传成功，文件ID: {}", filePO.getId());

            // 转换为VO返回
            return convertToVO(filePO);
        } catch (Exception e) {
            // 如果OSS已上传但后续失败，执行回滚删除
            if (fileUrl != null && objectName != null) {
                try {
                    ossUtil.deleteFile(objectName);
                    log.warn("已回滚删除OSS文件: {}", objectName);
                } catch (Exception ex) {
                    log.warn("回滚删除OSS文件失败: {} - {}", objectName, ex.getMessage());
                }
            }
            log.error("文件上传失败: {}", e.getMessage(), e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }
    
    @Override
    public FileVO getFileById(Long fileId) {
        FilePO filePO = fileMapper.selectById(fileId);
        if (filePO == null) {
            throw new RuntimeException("文件不存在");
        }
        
        return convertToVO(filePO);
    }
    @Override
    public boolean deleteFile(Long fileId, Long userId) {
        log.info("开始删除文件，文件ID: {}, 用户ID: {}", fileId, userId);
        
        // 查询文件信息
        FilePO filePO = fileMapper.selectById(fileId);
        if (filePO == null) {
            throw new RuntimeException("文件不存在");
        }
        
        // 权限验证：只有文件所有者才能删除
        if (!filePO.getUserId().equals(userId)) {
            throw new RuntimeException("无权限删除此文件");
        }
        
        try {
            // 从OSS删除文件
            String objectName = extractObjectNameFromUrl(filePO.getFileUrl());
            ossUtil.deleteFile(objectName);
            
            // 从数据库删除记录
            int result = fileMapper.deleteById(fileId);
            
            log.info("文件删除成功，文件ID: {}", fileId);
            return result > 0;
            
        } catch (Exception e) {
            log.error("文件删除失败，文件ID: {}", fileId, e);
            throw new RuntimeException("文件删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证文件
     * 
     * @param file 文件
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }
        
        // 验证文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("文件大小不能超过10MB");
        }
        
        // 验证文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new RuntimeException("文件名不能为空");
        }
        
        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!Arrays.asList(ALLOWED_TYPES).contains(extension)) {
            throw new RuntimeException("不支持的文件类型: " + extension);
        }
    }

    /**
     * 将传入的业务类型字符串解析为枚举并校验
     */
    private FilePO.BizType parseBizType(String bizType) {
        if (!StringUtils.hasText(bizType)) {
            throw new IllegalArgumentException("业务类型不能为空");
        }
        try {
            return FilePO.BizType.valueOf(bizType.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("不支持的业务类型: " + bizType);
        }
    }
    
    /**
     * 生成对象名称
     * 
     * @param originalFilename 原始文件名
     * @param bizType 业务类型
     * @return 对象名称
     */
    private String generateObjectName(String originalFilename, String bizType) {
        // 解析并使用规范化的业务类型作为路径
        FilePO.BizType bizTypeEnum = parseBizType(bizType);
        String extension = getFileExtension(originalFilename);
        if (!StringUtils.hasText(extension)) {
            throw new IllegalArgumentException("无法识别文件扩展名: " + originalFilename);
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String datePath = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return String.format("%s/%s/%s.%s", bizTypeEnum.name().toLowerCase(), datePath, uuid, extension);
    }
    
    /**
     * 生成缩略图URL
     * @param fileUrl 文件URL
     * @return 缩略图URL
     */
    private String generateThumbnailUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return null;
        }
        // 判断是否为图片文件
        String extension = getFileExtension(fileUrl).toLowerCase();
        String[] imageTypes = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};
        if (Arrays.asList(imageTypes).contains(extension)) {
            // 阿里云OSS图片处理参数：缩放到200x200
            return fileUrl + "?x-oss-process=image/resize,w_200,h_200,m_fill";
        }
        
        return null;
    }
    
    /**
     * 获取文件扩展名
     * 
     * @param filename 文件名
     * @return 扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        // 去除URL中的查询参数或片段
        int qIdx = filename.indexOf('?');
        if (qIdx != -1) {
            filename = filename.substring(0, qIdx);
        }
        int hashIdx = filename.indexOf('#');
        if (hashIdx != -1) {
            filename = filename.substring(0, hashIdx);
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        
        return filename.substring(lastDotIndex + 1);
    }
    
    /**
     * 从URL中提取对象名称
     * 
     * @param fileUrl 文件URL
     * @return 对象名称
     */
    private String extractObjectNameFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return "";
        }
        
        // 假设URL格式为: https://bucket.endpoint/objectName
        // 提取域名后的部分作为objectName
        try {
            java.net.URI uri = java.net.URI.create(fileUrl);
            String path = uri.getPath();
            // 去掉开头的斜杠
            return path.startsWith("/") ? path.substring(1) : path;
        } catch (Exception e) {
            log.warn("解析文件URL失败: {}", fileUrl, e);
            return "";
        }
    }
    
    /**
     * 转换为VO
     * 
     * @param filePO 文件PO
     * @return 文件VO
     */
    private FileVO convertToVO(FilePO filePO) {
        FileVO fileVO = new FileVO();
        fileVO.setId(filePO.getId());
        fileVO.setUserId(filePO.getUserId());
        fileVO.setBizType(filePO.getBizType().name());
        fileVO.setFileUrl(filePO.getFileUrl());
        fileVO.setThumbUrl(filePO.getThumbUrl());
        fileVO.setSize(filePO.getSize().longValue());
        fileVO.setCreatedAt(filePO.getCreatedAt());
        
        return fileVO;
    }
}
