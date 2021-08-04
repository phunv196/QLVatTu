/*
 * Copyright (C) 2010 HRPlus. All rights reserved.
 * HRPLUS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.app.model.category;

import javax.persistence.*;
import java.util.Date;

/**
 * Doi tuong file dinh kem
 *
 * @author NVH
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "attachment_file")
public class AttachmentFileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_file_id", unique = true, nullable = false) private Long attachmentFileId;
    @Column(name = "file_name") private String fileName;
    @Column(name = "type") private Long type;
    @Column(name = "object_id") private Long objectId;
    @Column(name = "description") private String description;
    @Column(name = "create_date") private Date createdDate;
    @Column(name = "created_by") private String createdBy;
    @Column(name = "server_attach_file") private String serverAttachFile;

    public AttachmentFileModel() {
    }

    public Long getAttachmentFileId() {
        return attachmentFileId;
    }

    public void setAttachmentFileId(Long attachmentFileId) {
        this.attachmentFileId = attachmentFileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getServerAttachFile() {
        return serverAttachFile;
    }

    public void setServerAttachFile(String serverAttachFile) {
        this.serverAttachFile = serverAttachFile;
    }
}
