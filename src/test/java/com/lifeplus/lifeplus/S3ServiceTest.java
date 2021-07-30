package com.lifeplus.lifeplus;

import com.amazonaws.services.s3.model.S3Object;
import com.lifeplus.lifeplus.service.S3Service;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @author Manuel Pedrozo
 * This is for manual testing, wont be able to do anything unless credentials are provided
 * as environment variables (accessKey and secretKey). Otherwise, everything but reading from
 * s3 will cause an exception.
 * Tests commented out to avoid running constantly. Dummy test to avoid "no tests to run" error.
 */
public class S3ServiceTest extends AbstractTest {

	@Autowired
	private S3Service s3Service;

	@Test
	public void dummyTest() {}

    //@Test
//	public void UploadFile() {
//        try {
//			File file = ResourceUtils.getFile("classpath:../resources/test1.bmp");
//			String url = s3Service.uploadFile("test1.bmp", file);
//			System.out.println("URL: " + url);
//		} catch (Exception e) {
//        	System.out.println(e);
//		}
//	}
//
//	//@Test
//	public void DownloadFile() {
//		S3Object s3Object = s3Service.downloadFile("test1.bmp");
//		assertNotNull(s3Object);
//		assertEquals("test1.bmp", s3Object.getKey());
//	}
}
