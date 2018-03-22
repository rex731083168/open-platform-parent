-------------------Phase 2.1.4.5 2018-01.04-------------------
db.getCollection('APIMG_APIS').update({},
   {$set:{'resourceType':'XGW_SERVICE'}},{multi:true})
   
-------------------Phase 2.4.4.6 2018-03.16-------------------   
ALTER TABLE `openplatform`.`api_detail` 
ADD COLUMN `request_body_type` VARCHAR(45) NULL AFTER `create_time`,
ADD COLUMN `response_body_type` VARCHAR(45) NULL AFTER `request_body_type`;

ALTER TABLE `openplatform`.`api_detail` 
CHANGE COLUMN `listen_path` `listen_path` VARCHAR(500) NULL DEFAULT NULL ,
CHANGE COLUMN `request_body_type` `request_body_type` VARCHAR(45) NULL DEFAULT NULL COMMENT '请求体类型：json/form' ,
CHANGE COLUMN `response_body_type` `response_body_type` VARCHAR(45) NULL DEFAULT NULL COMMENT '返回body类型：json等' ,
ADD COLUMN `protocol` VARCHAR(45) NULL COMMENT '协议：http/https' AFTER `response_body_type`,
ADD COLUMN `org_path` VARCHAR(500) NULL COMMENT '回源地址' AFTER `protocol`;

CREATE TABLE `openplatform`.`api_query_arg` (
  `uuid` INT(11) NOT NULL,
  `arg_id` VARCHAR(45) NULL,
  `api_id` VARCHAR(45) NULL,
  `arg_name` VARCHAR(45) NULL,
  `arg_type` VARCHAR(45) BINARY NULL,
  `required` TINYINT(1) NULL,
  `example` VARCHAR(5000) NULL,
  `arg_desc` VARCHAR(500) NULL,
  PRIMARY KEY (`uuid`));

-------------------Phase 2.4.4.6 2018-03.19-------------------  
ALTER TABLE `openplatform`.`api_query_arg` 
CHANGE COLUMN `uuid` `uuid` INT(11) NOT NULL AUTO_INCREMENT ;

-------------------Phase 2.4.4.6 2018-03.22-------------------
ALTER TABLE `openplatform`.`diy_apply` 
CHANGE COLUMN `apply_id` `apply_id` VARCHAR(45) NOT NULL COMMENT '定制应用主键，唯一标识' ,
CHANGE COLUMN `app_id` `app_id` VARCHAR(100) NULL DEFAULT NULL COMMENT '推送到产品中心后，回推的字段，很重要' ;

-------------------Phase 2.4.4.6 2018-03.22-------------------
CREATE TABLE `openplatform`.`saas` (
  `uuid` INT NOT NULL AUTO_INCREMENT,
  `id` VARCHAR(45) NOT NULL COMMENT 'saas表业务主键',
  `saas_id` VARCHAR(45) NOT NULL,
  `resource_type` VARCHAR(45) NOT NULL,
  `target_url` VARCHAR(200) NOT NULL,
  UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC),
  PRIMARY KEY (`uuid`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
COMMENT = 'saas表';

