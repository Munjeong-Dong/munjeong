package live.munjeong.server.app.file;

import live.munjeong.server.app.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
@TableGenerator(
        name = "FILE_SEQ_GENERATOR",
        table = "TB_SEQUENCES",
        pkColumnValue = "FILE_SEQ",
        allocationSize = 1)
@Entity
public class File extends BaseEntity {
    @Id
    @GeneratedValue(generator = "FILE_SEQ_GENERATOR", strategy = GenerationType.TABLE)
    @Column(name = "file_id", nullable = false)
    private Long id;

    // 업로드 파일 명
    private String originNm;
    // 저장 파일 명
    private String storageNm;
    /**
     *  저장 파일 위치
     *  파일 타입 / 년 / 월 / 일 / UUID
     *  ex ) 2022년 1월 14일 test.jpg -> IMAGE/2022/01/14/uuid
     */
    private String storagePath;

    // 파일 타입 (IMAGE, VIDEO, ATTACH)
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    // 파일 확장자
    private String extensions;

    //파일 사이즈
    private Long size;

    public File(String originNm, long size) {
        this.originNm = originNm;
        String extension = StringUtils.getFilenameExtension(originNm);
        this.extensions = extension == null? null : extension.toLowerCase();
        this.size = size;
        this.fileType = FileType.getFileType(extensions);

        this.storageNm = UUID.randomUUID().toString();
        this.storagePath = fileType.toString() + LocalDate.now().format(DateTimeFormatter.ofPattern("/yyyy/MM/dd/"));
    }

    public File(UploadFile upLoadFile) {
        this.originNm = upLoadFile.getUploadNm();
        this.extensions = upLoadFile.getExtension().toLowerCase();
        this.size = upLoadFile.getSize();
        this.fileType = FileType.getFileType(extensions);

        this.storageNm = UUID.randomUUID().toString();
        this.storagePath = fileType.toString() + LocalDate.now().format(DateTimeFormatter.ofPattern("/yyyy/MM/dd/"));
    }
}
