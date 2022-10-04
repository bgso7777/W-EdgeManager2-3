package com.inswave.appplatform.service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@TableGenerator(
name = "FILE_ATTACHEMENT_GENERATOR",
pkColumnValue = "FILE_ATTACHEMENT_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "file_attachement")
@Entity
public class FileAttachement extends StandardDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "FILE_ATTACHEMENT_GENERATOR")
    private         Long   id;
    @Column private String orgFileName;
    @Column private Long   fileSize;
    @Column private String fileType;
    @Column private String savePath;
}