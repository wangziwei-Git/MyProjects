package com.itheima;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

/**
 * 七牛云
 *      文件上传
 *      文件删除
 * @Author: wzw
 * @Date: 2020/11/11 18:16
 * @version: 1.8
 */
public class QiNiuTest {

    //@Test
    public void testUploadFile(){

        //1.构造一个带指定Zone对象的配置类，zone0表示华东地区（默认）
        // 我们的是创建时要的是华南,所以是2
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //2...生成上传凭证，然后准备上传
        String accessKey = "rQpB8ijXrPfb0g-KXrYSL6jYfDLfrIkN3lwEhFTb";//鉴证的账号(AK)
        String secretKey = "4NiqECa70HYpV7Ov-FMNFzG01FQzba9UBayjQ6Kh";//鉴证的密码(SK)
        String bucket = "wzw157106";//存储控件名称(服务器名)

        //3.如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\15710\\Desktop\\123.png";//本地文件路径

        //4.默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;//可以指定上传图片的名称
        //===========================改到这里就可以了=======================
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    //@Test
    public void testDeleteFile(){
        //1.构造一个带指定 Zone 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());

        //2....其他参数参考类注释
        String accessKey = "rQpB8ijXrPfb0g-KXrYSL6jYfDLfrIkN3lwEhFTb";//鉴证的账号(AK)
        String secretKey = "4NiqECa70HYpV7Ov-FMNFzG01FQzba9UBayjQ6Kh";//鉴证的密码(SK)
        String bucket = "wzw157106";//存储控件名称(服务器名)

        //3.需要删除的文件名称
        String key = "Fuo4PpNyqR8SayPeLSzkJBniyWBs";
        //===========================改到这里就可以了=======================
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }

    }
}
