package com.inswave.appplatform.wedgemanager.domain.terminal;

import com.inswave.appplatform.service.domain.StandardDomain;
import lombok.Data;

import javax.persistence.*;

@Data
@TableGenerator(
name = "TS_IMAGE_GENERATOR",
pkColumnValue = "TS_IMAGE_SEQ",
table = "EM_SEQ_GENERATOR",
allocationSize = 1)
@Table(name = "ts_image")
@Entity
public class TerminalImage extends StandardDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TS_IMAGE_GENERATOR")
    private         Long          id;
    @Column private String        fileName;
}