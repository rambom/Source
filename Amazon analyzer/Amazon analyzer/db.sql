    
    CREATE TABLESPACE AMAZON_ANALYZER
    LOGGING 
    DATAFILE 
    'D:\oracle\oradata\orcl\AMAZON_ANALYZER.ORA' SIZE 100M 
    REUSE AUTOEXTEND 
    ON NEXT  1024K MAXSIZE  32767M
    EXTENT MANAGEMENT LOCAL 
    SEGMENT SPACE MANAGEMENT  AUTO; 
    
       
    CREATE USER AMAZON_ANALYZER  PROFILE "DEFAULT" 
    IDENTIFIED BY "dbwork" DEFAULT TABLESPACE AMAZON_ANALYZER
    TEMPORARY TABLESPACE "TEMP" 
    ACCOUNT UNLOCK;
    
    GRANT CONNECT TO AMAZON_ANALYZER;
    GRANT DBA TO AMAZON_ANALYZER;
    ALTER USER "AMAZON_ANALYZER" DEFAULT ROLE  ALL;
	
create table MOVER_SHAKER_ASIN
(
  marketplace_id              VARCHAR2(50),
  gl_product_group            VARCHAR2(50),
  gl_product_group_desc       VARCHAR2(50),
  category_desc               VARCHAR2(100),
  subcategory_desc            VARCHAR2(100),
  asin                        VARCHAR2(10),
  item_name                   VARCHAR2(2000),
  brand_name                  VARCHAR2(300),
  ordered_gms_usd_last4wk     VARCHAR2(50),
  increased_gms_last4wk       VARCHAR2(50),
  increased_percent           VARCHAR2(50),
  fba_ordered_gms_usd_last4wk VARCHAR2(50),
  ordered_units_last4wk       VARCHAR2(50),
  fba_ordered_units_last4wk   VARCHAR2(50),
  asp                         VARCHAR2(50),
  increased_gms_rank          VARCHAR2(50)
);
create table MOVER_SHAKER_BRAND
(
  marketplace_id              VARCHAR2(50),
  gl_product_group            VARCHAR2(50),
  gl_product_group_desc       VARCHAR2(50),
  brand_name                  VARCHAR2(300),
  ordered_gms_usd_last4wk     VARCHAR2(50),
  increased_gms_last4wk       VARCHAR2(50),
  increased_percent           VARCHAR2(50),
  fba_ordered_gms_usd_last4wk VARCHAR2(50),
  ordered_units_last4wk       VARCHAR2(50),
  fba_ordered_units_last4wk   VARCHAR2(50),
  asp                         VARCHAR2(50),
  increased_gms_rank          VARCHAR2(50)
);
create table TOP_ASIN
(
  marketplace_id          VARCHAR2(10),
  gl_product_group        VARCHAR2(50),
  gl_product_group_desc   VARCHAR2(50),
  category_desc           VARCHAR2(100),
  subcategory_desc        VARCHAR2(100),
  asin                    VARCHAR2(10),
  item_name               VARCHAR2(2000),
  brand_name              VARCHAR2(300),
  ordered_gms_usd_ttm     VARCHAR2(50),
  fba_ordered_gms_usd_ttm VARCHAR2(50),
  ordered_units_ttm       VARCHAR2(50),
  fba_ordered_units_ttm   VARCHAR2(50),
  asp                     VARCHAR2(50),
  gms_rank                VARCHAR2(50),
  glance_view_count_ttm   VARCHAR2(50),
  units_conversion_rate   VARCHAR2(50)
);
create table TOP_ASIN_WITH_LIMITED_MATCH
(
  marketplace_id          VARCHAR2(50),
  gl_product_group        VARCHAR2(50),
  gl_product_group_desc   VARCHAR2(50),
  category_desc           VARCHAR2(100),
  subcategory_desc        VARCHAR2(100),
  asin                    VARCHAR2(10),
  item_name               VARCHAR2(2000),
  brand_name              VARCHAR2(300),
  offer_count             VARCHAR2(50),
  ordered_gms_usd_ttm     VARCHAR2(50),
  fba_ordered_gms_usd_ttm VARCHAR2(50),
  ordered_units_ttm       VARCHAR2(50),
  fba_ordered_units_ttm   VARCHAR2(50),
  asp                     VARCHAR2(50),
  gms_rank                VARCHAR2(50),
  glance_view_count_ttm   VARCHAR2(50),
  units_conversion_rate   VARCHAR2(50)
);
create table TOP_BRAND
(
  gl_product_group        VARCHAR2(50),
  gl_product_group_desc   VARCHAR2(50),
  marketplace_id          VARCHAR2(50),
  brand_name              VARCHAR2(300),
  ordered_gms_usd_ttm     VARCHAR2(50),
  fba_ordered_gms_usd_ttm VARCHAR2(50),
  ordered_units_ttm       VARCHAR2(50),
  fba_ordered_units_ttm   VARCHAR2(50),
  asp                     VARCHAR2(50),
  gms_rank                VARCHAR2(50)
);
create table TOP_CONVERSION_RATE
(
  marketplace_id          VARCHAR2(50),
  gl_product_group        VARCHAR2(50),
  gl_product_group_desc   VARCHAR2(50),
  category_desc           VARCHAR2(100),
  subcategory_desc        VARCHAR2(100),
  asin                    VARCHAR2(10),
  item_name               VARCHAR2(2000),
  brand_name              VARCHAR2(300),
  glance_view_count_ttm   VARCHAR2(50),
  ordered_gms_usd_ttm     VARCHAR2(50),
  fba_ordered_gms_usd_ttm VARCHAR2(50),
  ordered_units_ttm       VARCHAR2(50),
  fba_ordered_units_ttm   VARCHAR2(50),
  asp                     VARCHAR2(50),
  units_conversion_rate   VARCHAR2(50),
  gms_rank                VARCHAR2(50)
);
create table TOP_SELLER
(
  gl_product_group          VARCHAR2(50),
  gl_product_group_desc     VARCHAR2(50),
  marketplace_id            VARCHAR2(50),
  merchant_customer_id      VARCHAR2(50),
  merchant_friendly_name    VARCHAR2(200),
  registered_merchant_class VARCHAR2(50),
  current_merchant_status   VARCHAR2(50),
  launch_day                VARCHAR2(50),
  is_fba                    VARCHAR2(50),
  is_retail                 VARCHAR2(50),
  fba_launch_date           VARCHAR2(50),
  opportunity_owner         VARCHAR2(50),
  sf_country_of_origin      VARCHAR2(50),
  offering_count            VARCHAR2(50),
  fba_offering_count        VARCHAR2(50),
  gcid_seller               VARCHAR2(50),
  ordered_gms_usd_ttm       VARCHAR2(50),
  fba_ordered_gms_usd_ttm   VARCHAR2(50),
  ordered_units_ttm         VARCHAR2(50),
  fba_ordered_units_ttm     VARCHAR2(50),
  asp                       VARCHAR2(50),
  gms_rank                  VARCHAR2(50)
);
create table TOP_SUBCATEGORY
(
  marketplace_id          VARCHAR2(50),
  gl_product_group        VARCHAR2(50),
  gl_product_group_desc   VARCHAR2(50),
  category_desc           VARCHAR2(100),
  subcategory_desc        VARCHAR2(100),
  ordered_gms_usd_ttm     VARCHAR2(50),
  fba_ordered_gms_usd_ttm VARCHAR2(50),
  ordered_units_ttm       VARCHAR2(50),
  fba_ordered_units_ttm   VARCHAR2(50),
  asp                     VARCHAR2(50),
  gms_rank                VARCHAR2(50)
);
