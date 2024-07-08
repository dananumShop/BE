package org.dananum.dananum_shop.global.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.product.repository.ProductInformationImgRepository;
import org.dananum.dananum_shop.product.web.entity.ProductEntity;
import org.dananum.dananum_shop.product.web.entity.ProductInformationImgEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageUploadService {

    private final AmazonS3 amazonS3;

    private final ProductInformationImgRepository productInformationImgRepository;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    /**
     * 기능 - s3 이미지 업로드
     * @param image
     * @param folderName
     *
     * @return image_path
     */
    private String uploadImageToS3(MultipartFile image, String folderName) {
        String originName = image.getOriginalFilename();
        String ext = originName.substring(originName.lastIndexOf("."));
        String changedName = folderName + "/" + changedImageName(ext);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(Mimetypes.getInstance().getMimetype(changedName));

        try{
            byte[] bytes = IOUtils.toByteArray(image.getInputStream());
            metadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

            PutObjectResult putObjectResult = amazonS3.putObject(new PutObjectRequest(
                    bucketName, changedName, byteArrayIs, metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead));
            log.info("[UploadToS3] s3에 이미지가 업로드 되었습니다. resultUrl = " + putObjectResult);
        } catch (IOException e){
            throw new ImageUploadExeception();
        }

        return amazonS3.getUrl(bucketName, changedName).toString();
    }

    public List<ProductInformationImgEntity> uploadProductInformation(List<MultipartFile> images, String folderName, ProductEntity newProduct) {

        List<ProductInformationImgEntity> uploadedImages = new ArrayList<>();
        int index = 0;

        for(MultipartFile image : images){
            String originName = image.getOriginalFilename();
            String storedImagedPath = uploadImageToS3(image, folderName);

            log.debug("[UPLOAD] 이미지가 s3업데이트 메서드로 넘어갈 예정입니다. originName = " + originName);

            ProductInformationImgEntity newProductImage = ProductInformationImgEntity.from(originName, storedImagedPath, index, newProduct);

            productInformationImgRepository.save(newProductImage);

            uploadedImages.add(newProductImage);

            index++;
        }

        return uploadedImages;
    }



    private String changedImageName(String ext){
        String random = UUID.randomUUID().toString();
        return random+ext;
    }

    public class ImageUploadExeception extends RuntimeException{
        public ImageUploadExeception(){
            super("이미지 업로드 오류가 발생하였습니다.");
        }
    }
}
