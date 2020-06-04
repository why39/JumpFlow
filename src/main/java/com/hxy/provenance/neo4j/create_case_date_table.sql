-- 案件数据
CREATE TABLE `hxyframe_activiti`.`case_data` (
  `_id` INT NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `case_id` VARCHAR(255) NOT NULL COMMENT '案件id',
  `case_name` VARCHAR(255) NOT NULL COMMENT '案件名称',
  `case_category` VARCHAR(45) NULL COMMENT '案件类型',
  `user_id` VARCHAR(255) NULL COMMENT '案件发起人id',
  `user_name` VARCHAR(45) NULL COMMENT '案件发起人名称',
  `start_time` VARCHAR(45) NULL COMMENT '案件发起时间',
  `department_id` VARCHAR(5) NULL COMMENT '所属部门id',
  `department_name` VARCHAR(45) NULL COMMENT '所属部门名称',
  `status` INT NOT NULL DEFAULT 0 COMMENT '案件状态 0：整在办理，1：办理结束，-1：异常',
  PRIMARY KEY (`_id`),
  UNIQUE INDEX `_id_UNIQUE` (`_id` ASC));


-- 世系数据
CREATE TABLE `hxyframe_activiti`.`case_prov_tb` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `case_id` VARCHAR(255) NOT NULL COMMENT '案件id',
  `case_prov_data` BLOB NULL COMMENT '案件世系数据最新节点信息',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `case_id_UNIQUE` (`case_id` ASC));
