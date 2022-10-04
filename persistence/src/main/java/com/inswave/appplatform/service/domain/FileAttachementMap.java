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
name = "FILE_ATTACHEMENT_MAP_GENERATOR",
pkColumnValue = "FILE_ATTACHEMENT_MAP_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "file_attachement_map")
@Entity
public class FileAttachementMap extends StandardDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "FILE_ATTACHEMENT_MAP_GENERATOR")
    private                                                                              Long            id;
    @Column private                                                                      String          sourceType;
    @Column private                                                                      String          sourceCode;
    @ManyToOne(optional = false) @JoinColumn(name = "FILE_ID", nullable = false) private FileAttachement file;
}