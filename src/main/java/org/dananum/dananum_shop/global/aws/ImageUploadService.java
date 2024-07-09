package org.dananum.dananum_shop.global.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.product.repository.ProductDetailImgRepository;
import org.dananum.dananum_shop.product.repository.ProductInformationImgRepository;
import org.dananum.dananum_shop.product.web.entity.ProductDetailImgEntity;
import org.dananum.dananum_shop.product.web.entity.ProductEntity;
import org.dananum.dananum_shop.product.web.entity.ProductInformationImgEntity;
import org.dananum.dananum_shop.user.repository.UserProfileImgRepository;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.dananum.dananum_shop.user.web.entity.user.UserProfileImgEntity;
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
    private final ProductDetailImgRepository productDetailImgRepository;
    private final UserProfileImgRepository userProfileImgRepository;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    /**
     * 이미지를 Amazon S3에 업로드합니다.
     *
     * @param image 업로드할 이미지 파일을 나타내는 MultipartFile
     * @param folderName 이미지가 저장될 S3 폴더 이름
     * @return 업로드된 이미지의 URL
     * @throws ImageUploadExeception 업로드 중 IOException이 발생할 경우
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

    /**
     * 상품 정보 이미지를 Amazon S3에 업로드합니다.
     *
     * @param images 업로드할 이미지 파일 목록을 나타내는 List<MultipartFile>
     * @param folderName 이미지가 저장될 S3 폴더 이름
     * @param newProduct 이 이미지들과 연관된 ProductEntity
     */
    public void uploadProductInformation(List<MultipartFile> images, String folderName, ProductEntity newProduct) {

        int index = 0;

        for(MultipartFile image : images){
            String originName = image.getOriginalFilename();
            String storedImagedPath = uploadImageToS3(image, folderName);

            log.debug("[UPLOAD] 이미지가 s3업데이트 메서드로 넘어갈 예정입니다. originName = " + originName);

            ProductInformationImgEntity newProductImage = ProductInformationImgEntity.from(originName, storedImagedPath, index, newProduct);

            productInformationImgRepository.save(newProductImage);

            index++;
        }

    }

    /**
     * 상품 상세 이미지를 Amazon S3에 업로드합니다.
     *
     * @param images 업로드할 이미지 파일 목록을 나타내는 List<MultipartFile>
     * @param folderName 이미지가 저장될 S3 폴더 이름
     * @param newProduct 이 이미지들과 연관된 ProductEntity
     */
    public void uploadProductDetail(List<MultipartFile> images, String folderName, ProductEntity newProduct) {
        int index = 0;

        for(MultipartFile image : images){
            String originName = image.getOriginalFilename();
            String storedImagedPath = uploadImageToS3(image, folderName);

            log.debug("[UPLOAD] 이미지가 s3업데이트 메서드로 넘어갈 예정입니다. originName = " + originName);

            ProductDetailImgEntity newProductImage = ProductDetailImgEntity.from(originName, storedImagedPath, index, newProduct);

            productDetailImgRepository.save(newProductImage);

            index++;
        }
    }

    /**
     * 유저 프로필 이미지를 Amazon S3에 업로드합니다.
     *
     * @param image 업로드할 이미지 파일
     * @param folderName 이미지가 저장될 S3 폴더 이름
     * @param user 이 이미지와 연관된 UserEntity
     */
    public void uploadProfileImg(MultipartFile image, String folderName, UserEntity user) {
        String originName = image.getOriginalFilename();
        String storedImagedPath = uploadImageToS3(image, folderName);

        log.debug("[UPLOAD] 이미지가 s3업데이트 메서드로 넘어갈 예정입니다. originName = " + originName);

        UserProfileImgEntity newProfileImage = UserProfileImgEntity.from(originName, storedImagedPath, user);

        userProfileImgRepository.save(newProfileImage);
    }


    /**
     * 이미지 파일의 새로운 이름을 생성합니다.
     *
     * @param ext 이미지 파일의 확장자
     * @return 생성된 이미지 이름
     */
    private String changedImageName(String ext){
        String random = UUID.randomUUID().toString();
        return random+ext;
    }

    /**
     * 이미지 업로드 실패 시 발생하는 커스텀 예외입니다.
     */
    public class ImageUploadExeception extends RuntimeException{
        public ImageUploadExeception(){
            super("이미지 업로드 오류가 발생하였습니다.");
        }
    }

    /**
     * Amazon S3에서 이미지를 삭제합니다.
     *
     * @param productImagePath 삭제할 이미지의 URL
     */
    public void deleteImage(String productImagePath) {
        String key = extractKey(productImagePath);
        log.info("[test]" + key);
        DeleteObjectRequest deleteRequest = new DeleteObjectRequest(bucketName, key);
        amazonS3.deleteObject(deleteRequest);
    }

    /**
     * 이미지 URL에서 S3 키를 추출합니다.
     *
     * @param productImagePath 이미지의 URL
     * @return 이미지의 S3 키
     */
    private String extractKey(String productImagePath){
        String baseUrl = "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/";
        return productImagePath.substring(baseUrl.length());
    }
}
