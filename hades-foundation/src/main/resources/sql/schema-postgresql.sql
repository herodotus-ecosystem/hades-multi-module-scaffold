/*
 * Copyright (c) 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Project Name: hades-platform
 * Module Name: hades-foundation
 * File Name: schema-postgresql.sql
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

-------------安装加解密组件包-----------------------
CREATE EXTENSION pgcrypto;
-------------AES 解密，与Java 对应-----------------------
CREATE OR REPLACE FUNCTION "public"."f_aes_iv_decrypt"(varchar, varchar, varchar)
  RETURNS "pg_catalog"."varchar" AS $BODY$ DECLARE
	stringContent ALIAS FOR $1;
    aesKey ALIAS FOR $2;
    aesIv ALIAS FOR $3;
    decrypt_result VARCHAR;
BEGIN
	SELECT
		convert_from(
			decode(
				convert_from(
					decrypt_iv ( decode( LOWER ( stringContent ), 'hex' ), convert_to( aesKey, 'UTF8' ), convert_to( aesIv, 'UTF8' ), 'aes' ),
					'UTF8'
				),
				'base64'
			),
			'UTF8'
		) INTO decrypt_result;
	RETURN decrypt_result;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100

-------------AES 加密，与Java 对应-----------------------
CREATE OR REPLACE FUNCTION "public"."f_aes_iv_encrypt"(varchar, varchar, varchar)
  RETURNS "pg_catalog"."varchar" AS $BODY$
DECLARE
	stringContent ALIAS FOR $1;
	aesKey ALIAS FOR $2;
	aesIv ALIAS FOR $3;
	encrypt_result VARCHAR;
BEGIN
	SELECT UPPER
		(
			encode(
				encrypt_iv (
					convert_to( encode( stringContent, ' BASE64 ' ), ' UTF8 ' ),
					convert_to( aesKey, ' UTF8 ' ),
					convert_to( aesIv, ' UTF8 ' ),
					' aes '
				),
				' HEX '
			)) INTO encrypt_result;
	RETURN encrypt_result;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
---------------集成flowable自带用户体系，用view替换已有table，名字不变---------------------
CREATE
	OR REPLACE VIEW "public"."act_id_user" AS SELECT
	u.uid AS "id_",
	u.reversion AS "rev_",
	u.user_name AS "first_",
	'' AS "last_",
	f_aes_iv_decrypt ( e.full_name, '1234123412ABCDEF', 'ABCDEF1234123412' ) AS "display_name_",
	'' AS "email_",
	u."password" AS "pwd_",
	'' AS "picture_id_",
	'' AS "tenant_id_"
FROM
	sys_user u,
	sys_employee e
WHERE
	u.employeeid = e.pkid

---------------集成flowable自带用户体系，用view替换已有table，名字不变---------------------
CREATE
	OR REPLACE VIEW "public"."act_id_membership" AS SELECT
	ur.rid AS "user_id_",
	ur.uid AS "group_id_"
FROM
	sys_user_role ur
---------------集成flowable自带用户体系，用view替换已有table，名字不变---------------------
CREATE
	OR REPLACE VIEW "public"."act_id_group" AS SELECT
	r.rid AS "id_",
	r.reversion AS "rev_",
	r.role_name AS "name_",
	r.role_type AS "type_"
FROM
	sys_role r
