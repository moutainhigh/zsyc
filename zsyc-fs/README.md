# 文件管理系统

## 提供哪些功能
* 附件上传及下载
* 图片上传及下载

## 开发相关

## 调用说明

使用http协议进行文件上传

| _              | _                   |
|:---------------|:--------------------|
| Request Path   | upload              |
| Request Method | POST                |
| Content-Type   | multipart/form-data |
| Request Param  | file                |

调用 demo

```shell
curl --request POST \
  --url http://14.29.207.159:9904/upload \
  --header 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  --form file=@/path-file/file.jpg
```

### 返回说明


```json
{
    "id": 222,
    "name": "pp.jpg",
    "suffix": "jpg",
    "code": "874F3E47DD3FD68332A83B716332D9FA6B606BB8",
    "path": "/874/874F3E47DD3FD68332A83B716332D9FA6B606BB8",
    "contentType": "image/jpeg",
    "createTime": 1512512116000,
    "url": "/874/874F3E47DD3FD68332A83B716332D9FA6B606BB8.jpg"
}
```
*注* 核心信息是`code`


## 判断文件是否存在

调用 demo

```shell
curl -X GET \
  'http://14.29.207.159:9904/exist?code=874F3E47DD3FD68332A83B716332D9FA6B606BB8' 
```

| _              | _                   |
|:---------------|:--------------------|
| Request Path   | exist              |
| Request Method | get                |
| Content-Type   | - -                |
| Request Param  | code                |

### 返回说明

> 与上传一致


## 文件访问

```
	${host}/download/${code}.any
```


## 图片访问

| 前缀   | 尺寸      | 格式                          | 例子                                                                          |
|:-------|:----------|:------------------------------|:------------------------------------------------------------------------------|
| origin | 原始      | `${host}`/origin/`${code}` | http://14.29.207.159:9904/origin/874F3E47DD3FD68332A83B716332D9FA6B606BB8 |
| middle | 245 x 245 | `${host}`/middle/`${code}` | http://14.29.207.159:9904/middle/874F3E47DD3FD68332A83B716332D9FA6B606BB8 |
| small  | 155 x 100 | `${host}`/small/`${code}` | http://14.29.207.159:9904/small/874F3E47DD3FD68332A83B716332D9FA6B606BB8  |


# nginx 直接访问图片

需要安装`http_image_filter_module`模块，nginx配置参考

```nginx

server {
  listen      9905;
  server_name 14.29.207.159;

  # include ssl_conf;

  root /data/upload/zsyc-fs;

  location ~ ^/small(/...)([^\.\-]*)(\.\w*)?$ {
    set $file $1$1$2;
    image_filter resize 155 100;
    #simage_filter_jpeg_quality $quality;
    image_filter_interlace on;
    image_filter_transparency on;
    image_filter_buffer 8M;
    try_files $file =404;
  }
  location ~ ^/middle(/...)([^\.\-]*)(\.\w*)?$ {
    set $file $1$1$2;
    image_filter resize 245 245;
    #image_filter_jpeg_quality $quality;
    image_filter_interlace on;
    image_filter_transparency on;
    image_filter_buffer 8M;
    try_files $file =404;
  }
  location ~ ^/origin(/...)([^\.\-]*)(\.\w*)?$ {
    set $file $1$1$2;
    image_filter_jpeg_quality 8;
    image_filter_interlace on;
    image_filter_transparency on;
    image_filter_buffer 8M;
    try_files $file =404;
  }
  location ~ ^(/...)([^\.\-]*)(\.\w*)?$ {
    add_header Content-Type 'image/*';
    set $file $1$1$2;
    try_files $file =404;
  }
  location ~ ^(/...)([^\.\-]*)-(\d*)[Xx](\d*)@?(\d*)?(\.\w*)?$ {
    set $file $1$1$2;
    set $width $3;
    set $height $4;
    set $rotate $5;

    image_filter resize $width $height;
    image_filter rotate $rotate;
    image_filter_interlace on;
    image_filter_transparency on;
    image_filter_buffer 8M;
    try_files $file $uri;
  }
}

```

## 访问方式

- host `host_name`

- 兼容原来的[图片访问](#user-content-图片访问)
- 直接访问原始图片`${host}`/`${code}`
  http://14.29.207.159:9905/874F3E47DD3FD68332A83B716332D9FA6B606BB8
- 自定义尺寸图片`${host}`/`${code}`-`width`[xX]`height`@`rotate`.`xxx`
  http://14.29.207.159:9904/874F3E47DD3FD68332A83B716332D9FA6B606BB8-360x360@180
  - 其中`rotate`只能为`90`倍数时才会生效
  - 【@`rotate`】【.`xxx`】都是可选项


