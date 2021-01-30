-- 运营管理数据库
create database mgt;

-- 销售品表
create table mgt_production(
id varchar(21) primary key COMMENT '主键',
sale_code varchar(20) COMMENT '销售品编码',
name varchar(100) COMMENT '销售品名称',
status smallint COMMENT '状态 0有效,1无效',
channel  smallint COMMENT '位置渠道',
tenant_id varchar(20) COMMENT '租户id',
creater_id varchar(20) COMMENT '创建人id',
updater_id varchar(20) COMMENT '修改人id',
create_time datetime COMMENT '创建时间',
creater_name varchar(20) COMMENT '创建人',
update_time datetime COMMENT '更新时间',
effective_start_time datetime COMMENT '生效时间',
invalid_start_time datetime COMMENT '失效时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '销售品';

alter table mgt_production add unique (sale_code);

-- 销售品关联api关系表
create table mgt_production_api_relation(
id varchar(21) primary key COMMENT '主键',
product_id varchar(21) COMMENT '销售品id',
api_id varchar(21) COMMENT 'apiId',
status smallint  COMMENT '状态 0有效,1无效',
tenant_id varchar(20) COMMENT '租户id',
creater_name varchar(20) COMMENT '创建人',
creater_id varchar(20) COMMENT '创建人id',
updater_id varchar(20) COMMENT '修改人id',
create_time datetime COMMENT '创建时间',
update_time datetime COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '销售品关联api关系表';

-- 唯一索引
alter table  mgt_production_api_relation add unique (product_id,api_id);

-- api表
create table mgt_api(
id varchar(21) primary key COMMENT'主键',
name varchar(100) COMMENT 'api名称',
url varchar(100) COMMENT 'url地址',
description varchar(100) COMMENT'api描述',
api_program_name varchar(100) COMMENT 'api程序名称',
api_category varchar(100) COMMENT 'api服务类型',
extro_path varchar(255) COMMENT '外部路径',
internal_path varchar(255) COMMENT '内部路径',
micro_name varchar(100) COMMENT '微服务名称',
micro_program_name varchar(100) COMMENT '微服务程序名称',
status smallint COMMENT '状态',
tenant_id varchar(20) COMMENT '租户id',
creater_id varchar(20) COMMENT '创建人id',
creater_name varchar(20) COMMENT '创建人',
create_time datetime COMMENT '创建时间',
updater_id varchar(20) COMMENT '修改人id',
update_time datetime COMMENT '修改时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = 'api表';

-- api目录
create table mgt_api_catalog(
id varchar(21) primary key COMMENT'主键',
parent_id varchar(21) COMMENT '父级ID',
seq_num smallint COMMENT '展示顺序',
name varchar(100) COMMENT '目录名称',
description varchar(100) COMMENT '目录描述',
level smallint COMMENT '目录级别',
status smallint COMMENT '状态',
tenant_id varchar(20) COMMENT '租户ID',
creater_id varchar(20) COMMENT '创建人id',
creater_name varchar(20) COMMENT '创建人',
updater_id varchar(20) COMMENT '更新人ID',
create_time datetime COMMENT '创建时间',
update_time datetime COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = 'api目录表';

alter table mgt_api_catalog add index index_parent_id(parent_id);

-- api和目录关系表
create table mgt_api_relation_catalog(
id varchar(21) primary key COMMENT'主键',
api_id varchar(21) COMMENT 'apiid',
api_catalog_id varchar(21) COMMENT '目录id',
status smallint COMMNET '状态',
tenant_id varchar(20) COMMENT '租户id',
creater_id varchar(20) COMMENT '创建人id',
creater_name varchar(20) COMMENT '创建人',
create_time datetime COMMENT '创建时间',
updater_id varchar(20) COMMENT '修改人id',
update_time datetime COMMENT '修改时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = 'api和目录关系表';

alter table mgt_api_relation_catalog add unique (api_id,api_catalog_id);

-- api策略表
create table mgt_api_strategy(
id varchar(21) primary key COMMENT'主键',
api_id varchar(21) COMMENT 'apiid',
month_limit int COMMENT '每月月限制上限',
day_limit	int COMMENT '每天限制上限',
second_limit	int COMMENT '每秒限制上限',
day_from	varchar(20) COMMENT '每天开始时间',
day_to	varchar(20) COMMENT '每天结束时间',
status	smallint COMMENT '状态',
tenant_id	varchar(20) COMMENT '租户',
creater_id	varchar(20) COMMENT '创建人id',
creater_name varchar(20) COMMENT '创建人',
create_time	datetime COMMENT '创建时间',
updater_id	varchar(20) COMMENT '修改人id',
update_time	datetime COMMENT '修改时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = 'api策略表';

alter table mgt_api_strategy add unique (api_id);

-- 客户表
create table mgt_customer(
id varchar(21) primary key COMMENT'主键',
customer_code	varchar(20)	COMMENT '客户代码',
customer_business_code	varchar(20)	COMMENT '客户代码',
name	varchar(100) COMMENT '客户名称',
ip	varchar(100) COMMENT 'ip地址',
industry_list varchar(1024) COMMENT '行业',
charge	smallint COMMENT '1计费，0不计费',
phone_num	varchar(20)	COMMENT '电话号码',
email	varchar(100) COMMENT '邮箱地址',
type	varchar(20)	COMMENT '属性',
`limit`	smallint COMMENT '客户权限1周期2紧急定位3单次',
customer_status varchar(21) COMMENT '业务状态	1、商用2、试商用3、测试',
service_way	varchar(21)	COMMENT '接入方式',
level	varchar(21)	COMMENT '优先级',
level_seq varchar(21) COMMENT '优先级顺序',
SaaS	smallint	COMMENT '1开通，0不开通',
status	smallint	COMMENT '1 有效 0 无效',
tenant_id	VARCHAR(21) COMMENT '租户ID',
creater_id	Varchar(21) COMMENT '创建人ID',
create_time	datetime COMMENT '创建时间',
creater_name varchar(20) COMMENT '创建人',
updater_id	varchar(21) COMMENT '更新人ID',
update_time	datetime COMMENT '更新时间',
srcTerm_id varchar(20) COMMENT '特服号',
short_msg smallint  COMMENT '是否开通短报文 1开通 0不开通'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '客户表';

alter table mgt_customer add unique (customer_code,name);
alter table mgt_customer add index customer_name_index(name);
-- 终端用户管理表
create table mgt_terminal(
id varchar(21) primary key COMMENT'主键',
user_name varchar(100) COMMENT '用户名称',
msisdn	char(21) COMMENT '终端号码',
imsi	varchar(20)	COMMENT	'国际移动用户识别码',
industry	varchar(20)	COMMENT '行业',
url     varchar(100) COMMENT 'url地址',
type	varchar(20) COMMENT '终端类型',
user_status	varchar(20)	COMMENT '用户状态',
charge	smallint COMMENT '1计费，0不计费',
source	varchar(20) COMMENT'数据来源',
customer_id varchar(22) COMMENT	 '客户ID',
status	smallint COMMENT '0 有效 1 无效',
tenant_id	varchar(21)	COMMENT '租户ID',
creater_id	varchar(21)	COMMENT '创建人ID',
creater_name varchar(20) COMMENT '创建人',
create_time	datetime COMMENT '创建时间',
updater_id	varchar(21)	COMMENT '更新人ID',
update_time	datetime COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '终端用户管理表';

alter table mgt_terminal add unique (user_name,msisdn);
alter table mgt_terminal add index customer_inx(customer_id);
alter table mgt_terminal add terminal_factory varchar(100) COMMENT '终端厂商';
-- 业务标识表
create table mgt_service(
id varchar(21) primary key COMMENT'主键',
service_code varchar(20) COMMENT '业务标识',
customer_code varchar(21)	COMMENT '客户代码' ,
product_id	varchar(21)	COMMENT '销售品ID',
start_time	datetime	COMMENT '服务开始时间',
end_time	datetime	COMMENT '服务结束时间',
special_service_number	varchar(20)	COMMENT '特服号',
callback_url	varchar(255)	COMMENT '回调地址',
platform_ip_list	varchar(255)	COMMENT 'IP地址","隔开',
status	smallint	COMMENT '0 有效 1 无效',
tenant_id	varchar(21) COMMENT '租户ID',
creater_id	varchar(21)	COMMENT	'创建人ID',
create_time	datetime	COMMENT '创建时间',
creater_name varchar(20) COMMENT '创建人',
updater_id	varchar(21) COMMENT '更新人ID',
update_time	datetime COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '业务标识表';

alter table mgt_service add unique (service_code);
alter table mgt_service add index customer_code_idx (customer_code);
alter table mgt_service add service_type varchar(10) COMMENT '业务类型';

-- 客户策略表
create table mgt_service_strategy(
id varchar(21) primary key COMMENT'主键',
service_id	varchar(21)	COMMENT 'id为0存系统全局配置',
second_limit int	COMMENT '每秒上限',
day_limit	int	COMMENT	'每天上限',
month_limit	int	COMMENT	'每月上限',
day_from	varchar(20)	COMMENT '每天开始时间',
day_to	varchar(20)	COMMENT '每天结束时间',
status	smallint	COMMENT '0 有效 1 无效',
tenant_id	varchar(21)	COMMENT	'租户ID',
creater_id	varchar(21)	COMMENT'创建人ID',
creater_name varchar(20) COMMENT '创建人',
create_time	datetime COMMENT'创建时间',
updater_id	varchar(21)	COMMENT	'更新人ID',
update_time	datetime COMMENT'更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '客户策略表';

alter table mgt_service_strategy add unique (service_id);

-- 终端回调关系表
create table mgt_terminal_callback_service(
id varchar(21) primary key COMMENT'主键',
terminal_id	varchar(21)	COMMENT'终端id',
api_category	varchar(20)	COMMENT '服务类型（配置表code）',
service_id	varchar(21)	COMMENT '业务标识ID',
status	smallint COMMENT '0 有效 1 无效',
tenant_id	varchar(21)	COMMENT '租户ID',
creater_id	varchar(21)	COMMENT	'创建人ID',
creater_name varchar(20) COMMENT '创建人',
create_time	datetime COMMENT '创建时间',
updater_id	varchar(21)	COMMENT '更新人ID',
update_time	datetime COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '终端回调关系表';

alter table mgt_terminal_callback_service add unique (terminal_id,api_category);
alter table mgt_terminal_callback_service add index  (service_id);

--终端白名单
create table mgt_terminal_white(
id varchar(21) primary key COMMENT'主键',
service_code varchar(25)	COMMENT'业务标识',
terminal_id	varchar(21) COMMENT '终端id',
terminal_factory varchar(100) COMMENT '终端厂商',
status	smallint	COMMENT '状态0 有效 1 无效',
tenant_id	varchar(21) COMMENT'租户ID',
creater_id	varchar(21)	COMMENT	'创建人ID',
creater_name varchar(20) COMMENT '创建人',
create_time	datetime	COMMENT'创建时间',
updater_id	varchar(21)	COMMENT'更新人ID',
update_time	datetime COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '终端白名单表';

alter table mgt_terminal_white add unique (service_code,terminal_id);
alter table mgt_terminal_white add index terminal_index (terminal_id);

-- 终端黑名单
create table mgt_terminal_black(
id varchar(21) primary key COMMENT'主键',
level	varchar(20) COMMENT	'等级：customer/terminal_sys/terminal_idt',
service_code varchar(25)	COMMENT	'客户级业务标识',
terminal_id	varchar(21)  COMMENT'终端ID',
customer_code	varchar(21)  COMMENT'客户代码',
type	varchar(20)	COMMENT'MO/MT',
status	smallint	COMMENT'1 有效 0 无效',
tenant_id	varchar(21) COMMENT'租户ID',
creater_id	varchar(21)	COMMENT'创建人ID',
creater_name varchar(20) COMMENT '创建人',
create_time	datetime COMMENT'创建时间',
updater_id	varchar(21) COMMENT '更新人ID',
update_time	datetime COMMENT'更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '终端黑名单表';

-- alter table mgt_terminal_black add unique (level,type,service_code);
alter table mgt_terminal_black add index terminal_index (terminal_id);


-- IP白名单
create table mgt_ip_white(
id varchar(21) primary key COMMENT'主键',
customer_id	varchar(21)	 COMMENT '客户ID',
ip_protocal	varchar(5) 	COMMENT	'1 IPV4,2：IPV6',
ip	varchar(100)	 COMMENT 'IP地址',
status	smallint COMMENT'0 有效 1 无效',
tenant_id	varchar(21)	 COMMENT '租户ID',
creater_id	varchar(21)	COMMENT '创建人ID',
creater_name varchar(20) COMMENT '创建人',
create_time	datetime COMMENT'创建时间',
updater_id	varchar(21)	COMMENT	'更新人ID',
update_time	datetime COMMENT'更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = 'IP白名单表';

alter table mgt_ip_white add unique (customer_id,ip);

--serverId管理表
create table mgt_serverid(
id varchar(21) primary key COMMENT'主键',
server_id	varchar(21)	COMMENT'平台标识',
api_category	varchar(50)	COMMENT'服务类型',
status	smallint COMMENT'0 有效 1 无效',
tenant_id	varchar(21)	COMMENT'租户ID',
creater_id	varchar(21)	COMMENT'创建人ID',
creater_name varchar(20) COMMENT '创建人',
create_time	datetime	COMMENT '创建时间',
updater_id	varchar(21)	COMMENT'更新人ID',
update_time	datetime COMMENT'更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = 'serverId管理表';

-- 租户和客户编码关系表
create table mgt_customer_tenant_relation(
id varchar(21) primary key COMMENT'主键',
customer_code	varchar(21)  COMMENT'客户代码',
tenant_id	varchar(21)	COMMENT'租户ID'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '客户和租户关系表';

alter table mgt_customer_tenant_relation add index (customer_code);

--終端设备管理表
create table mgt_terminal_device(
id varchar(21) primary key COMMENT'主键',
imei varchar(21) COMMENT '国际移动设备识别码',
type smallint COMMENT '终端类型',
terminal_factory varchar(100) COMMENT '终端厂商',
model varchar(51) COMMENT '终端型号',
meid varchar(20) COMMENT '移动设备识别码',
audit_status smallint	COMMENT '审核状态  1待提交 2待审核 3审核通过 4审核驳回 5申请删除',
tenant_id	varchar(21) COMMENT'租户ID',
creater_id	varchar(21)	COMMENT'创建人ID',
creater_name varchar(20) COMMENT '创建人',
create_time	datetime COMMENT'创建时间',
updater_id	varchar(21) COMMENT '更新人ID',
update_time	datetime COMMENT'更新时间',
status	smallint COMMENT'0 已删除 1 正常',
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '終端设备管理表';

alter table mgt_terminal_device add unique (imei,meid);
alter table mgt_terminal_device add index (imei);

--设备审核详情表
create table mgt_device_audit_detail(
id varchar(21) primary key COMMENT'主键',
audit_id varchar(22) COMMENT '审核设备ID',
audit_result smallint COMMENT '审核结果  1通过 0驳回',
audit_name varchar(20) COMMENT '审核人员',
audit_time	datetime COMMENT '审核时间',
remark varchar(400) COMMENT '备注',
tenant_id	varchar(21) COMMENT'租户ID',
creater_id	varchar(21)	COMMENT'创建人ID',
creater_name varchar(20) COMMENT '创建人',
create_time	datetime COMMENT'创建时间',
updater_id	varchar(21) COMMENT '更新人ID',
update_time	datetime COMMENT'更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '设备审核详情表';

--终端厂商管理表
create table mgt_terminal_factory(
id varchar(21) primary key COMMENT'主键',
factory_name varchar(100) COMMENT '厂商名称',
corporation_name varchar(100) COMMENT '法人名称',
factory_phonenum varchar(20)	COMMENT '厂商电话',
factory_address varchar(100) COMMENT '厂商地址',
factory_status smallint COMMENT '状态 1正常 0注销',
tenant_id	varchar(21) COMMENT'租户ID',
creater_id	varchar(21)	COMMENT'创建人ID',
creater_name varchar(20) COMMENT '创建人',
create_time	datetime COMMENT'创建时间',
updater_id	varchar(21) COMMENT '更新人ID',
update_time	datetime COMMENT'更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '终端厂商管理表';
alter table mgt_terminal_factory add unique (factory_name);

--同步核心网设备信息表
create table mgt_device_info(
id varchar(21) primary key COMMENT'主键',
operate smallint COMMENT'操作类型 1标识新增 2标识删除',
imei varchar(21) COMMENT'国际移动设备识别码',
desc varchar(100) COMMENT'描述信息'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '同步核心网设备信息管理表';
alter table mgt_terminal_factory add index (id);

--密钥管理表
create table mgt_secret_key(
id varchar(21) primary key COMMENT'主键',
key_source smallint COMMENT'密钥来源 1界面 2接口',
key_version varchar(100) COMMENT'密钥版本',
customer_code varchar(21)  COMMENT'客户代码',
secret_key varchar(100) COMMENT'密钥',
key_status	smallint COMMENT'密钥状态 1 有效 0 无效',
tenant_id	varchar(21) COMMENT'租户ID',
creater_id	varchar(21)	COMMENT'创建人ID',
creater_name varchar(20) COMMENT '创建人',
create_time	datetime COMMENT'创建时间',
updater_id	varchar(21) COMMENT '更新人ID',
update_time	datetime COMMENT'更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '密钥管理表';

--密钥历史记录管理表
create table mgt_key_history(
id varchar(21) primary key COMMENT'主键',
key_id varchar(21)  COMMENT'密钥ID',
key_version varchar(100) COMMENT'密钥版本',
secret_key varchar(100) COMMENT'密钥',
key_status smallint COMMENT'密钥状态 1 有效 0 无效',
key_start_time datetime COMMENT'有效开始时间',
key_end_time datetime COMMENT'有效结束时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '密钥历史记录管理表';

-- 终端版本协议管理
create table mgt_terminal_version_protocolmanagement(
id varchar(21) primary key COMMENT'主键',
terminal_version varchar(30) COMMENT '版本号',
protocol_identity varchar(30) COMMENT '协议标识',
terminal_type smallint COMMENT '终端类型',
terminal_factory varchar(128) COMMENT '终端厂商',
protocol_desc varchar(500) COMMENT '协议描述',
status smallint COMMENT '状态 1有效,0无效',
creater_id	varchar(21)	COMMENT'创建人ID',
creater_name varchar(20) COMMENT '创建人',
create_time	datetime COMMENT'创建时间',
updater_id	varchar(21) COMMENT '更新人ID',
updater_name	varchar(21) COMMENT '更新人',
update_time	datetime COMMENT'更新时间',
tenant_id varchar(30) COMMENT '租户'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '终端版本协议管理';

alter table mgt_terminal_version_protocolmanagement add index(terminal_version);


--导入异常信息管理表
create table mgt_upload_error(
`key` varchar(100)  COMMENT'异常信息键',
key_text LONGTEXT COMMENT'异常信息字符串'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '导入异常信息管理表';
alter table mgt_upload_error add unique (`key`);