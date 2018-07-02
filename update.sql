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
-------------------Phase 2.4.4.6 2018-03.23-------------------
ALTER TABLE `openplatform`.`api_result_example` 
CHANGE COLUMN `rex_value` `rex_value` LONGTEXT NULL DEFAULT NULL ;

-------------------Phase 2.4.5.6 2018-03.28-------------------
ALTER TABLE `openplatform`.`api_arg` 
ADD COLUMN `is_import` TINYINT(1) NULL DEFAULT 0 AFTER `arg_desc`;

ALTER TABLE `openplatform`.`api_detail` 
CHANGE COLUMN `listen_path` `listen_path` VARCHAR(500) NULL DEFAULT NULL ;

ALTER TABLE `openplatform`.`api_detail` 
ADD COLUMN `request_body_type` VARCHAR(45) NULL COMMENT '请求体类型：json/form' AFTER `create_time`,
ADD COLUMN `response_body_type` VARCHAR(45) NULL COMMENT '返回body类型：json等' AFTER `request_body_type`,
ADD COLUMN `protocol` VARCHAR(45) NULL COMMENT '协议：http/https' AFTER `response_body_type`,
ADD COLUMN `org_path` VARCHAR(500) NULL COMMENT '回源地址' AFTER `protocol`;

CREATE TABLE `api_query_arg` (
  `uuid` int(11) NOT NULL AUTO_INCREMENT,
  `arg_id` varchar(45) DEFAULT NULL,
  `api_id` varchar(45) DEFAULT NULL,
  `arg_name` varchar(45) DEFAULT NULL,
  `arg_type` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `required` tinyint(1) unsigned DEFAULT NULL,
  `example` varchar(5000) DEFAULT NULL,
  `arg_desc` varchar(500) DEFAULT NULL,
  `is_import` tinyint(1) unsigned DEFAULT '0',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=729 DEFAULT CHARSET=utf8;

ALTER TABLE `openplatform`.`api_result_example` 
CHANGE COLUMN `rex_value` `rex_value` LONGTEXT NULL DEFAULT NULL ;

CREATE TABLE `saas` (
  `uuid` int(11) NOT NULL AUTO_INCREMENT,
  `id` varchar(45) NOT NULL COMMENT 'saas表业务主键',
  `saas_id` varchar(45) NOT NULL,
  `resource_type` varchar(45) NOT NULL,
  `target_url` varchar(200) NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uuid_UNIQUE` (`uuid`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='saas表';

CREATE TABLE `dubbo_apply` (
  `pkid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `id` varchar(500) DEFAULT NULL COMMENT 'id',
  `parentId` varchar(500) DEFAULT NULL COMMENT '父id',
  `appName` varchar(255) DEFAULT NULL COMMENT '应用名称-接口获取',
  `appId` varchar(255) DEFAULT NULL COMMENT '应用id-接口获取',
  `appCode` varchar(255) DEFAULT NULL COMMENT '应用code-接口获取',
  `unitType` varchar(255) DEFAULT NULL COMMENT '接口所属单位   100000 平台\r\n                     100001 官网\r\n                     100002 营销宝\r\n                     100003  扫码购\r\n                     100004  酒店\r\n                     100007 高程\r\n                     100008 电商\r\n                     166666 中台',
  `type` varchar(255) DEFAULT NULL COMMENT 'jar class method',
  `packageName` varchar(255) DEFAULT NULL,
  `className` varchar(255) DEFAULT NULL,sss
  `interFaceRealName` varchar(255) DEFAULT NULL COMMENT '接口真实名称',
  `interFaceName` varchar(255) DEFAULT NULL COMMENT '自定义注解 name 名称',
  `interFaceDes` varchar(255) DEFAULT NULL COMMENT '自定义注解 des 描述',
  `interFaceVersion` varchar(255) DEFAULT NULL COMMENT '自定义注解 version 版本',
  `fileName` varchar(255) DEFAULT NULL,
  `filePath` varchar(255) DEFAULT NULL COMMENT 'jar file path',
  `filetag` varchar(255) DEFAULT NULL COMMENT 'jar type : 1 service, 2 depency',
  `creatTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatTime` datetime DEFAULT NULL,sss
  `creatUserName` varchar(255) DEFAULT NULL COMMENT '创建人',
  `creatUserId` varchar(255) DEFAULT NULL COMMENT '创建用户ID',
  PRIMARY KEY (`pkid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-------------------Phase 2.4.5.6 2018-03.29-------------------
ALTER TABLE `openplatform`.`api_result` 
CHANGE COLUMN `ret_name` `ret_name` VARCHAR(200) NULL DEFAULT NULL ;

ALTER TABLE `openplatform`.`api_arg` 
CHANGE COLUMN `arg_name` `arg_name` VARCHAR(200) NULL DEFAULT NULL ;

ALTER TABLE `openplatform`.`api_query_arg` 
CHANGE COLUMN `arg_name` `arg_name` VARCHAR(200) NULL DEFAULT NULL ;

-------------------Phase 2.4.5.6 2018-03.30-------------------
CREATE TABLE `openplatform`.`sand_box` (
  `uuid` INT NOT NULL AUTO_INCREMENT,
  `box_id` VARCHAR(45) NOT NULL,
  `name` VARCHAR(100) NULL,
  `template_name` VARCHAR(45) NULL,
  `box_url` VARCHAR(200) NULL,
  `resource_pool` VARCHAR(200) NULL,
  `create_state` VARCHAR(45) NULL,
  `state` TINYINT(1) UNSIGNED NULL,
  `deleted` VARCHAR(45) NULL,
  `create_date` DATETIME NULL,
  `delete_date` DATETIME NULL,
  `user_id` VARCHAR(45) NULL,
  `enterprise_name` VARCHAR(45) NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE INDEX `box_id_UNIQUE` (`box_id` ASC),
  UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC));
  
-------------------Phase 2.4.5.6 2018-03.31-------------------  
ALTER TABLE `openplatform`.`saas` 
ADD COLUMN `sandbox_id` VARCHAR(45) NULL AFTER `target_url`,
ADD COLUMN `create_date` DATETIME NULL AFTER `sandbox_id`,
ADD COLUMN `update_date` DATETIME NULL AFTER `create_date`;
ADD COLUMN `route_id` VARCHAR(45) NULL AFTER `uuid`;


------------------- 未执行SQL by lida 解决showApi接口查询慢得问题 增加API相关表的索引 @makangwei 进行验证后更新生产数据库 --------
ALTER  TABLE  `api_header`  ADD  INDEX header_index_api_id (`api_id`);

ALTER  TABLE  `api_arg`  ADD  INDEX arg_index_api_arg (`api_id`);

ALTER  TABLE  api_query_arg  ADD  INDEX query_index_api_arg (`api_id`);

ALTER  TABLE  api_result ADD  INDEX result_index_api_arg (`api_id`);

ALTER  TABLE  api_result_example ADD  INDEX example_index_api_arg (`api_id`);

ALTER  TABLE  api_code ADD  INDEX code_index_api_arg (`api_id`);

ALTER  TABLE  api_detail ADD  INDEX header_index_api_arg (`api_id`);

