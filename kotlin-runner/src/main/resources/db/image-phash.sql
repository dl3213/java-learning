-- ============================================================
-- pHash 感知哈希方案(图片近似重复检测)
-- 64bit DCT pHash,16 位十六进制字符串存储
-- 与 image-similarity.sql(CLIB 向量方案)互不影响
-- ============================================================

ALTER TABLE t_base_file ADD COLUMN IF NOT EXISTS phash VARCHAR(16);

-- 部分索引:仅已回填且未删除的行;查询走全量内存比对,此索引主要用于存在性过滤
CREATE INDEX IF NOT EXISTS idx_base_file_phash
    ON t_base_file (phash)
    WHERE phash IS NOT NULL AND is_deleted = '0';

-- ============================================================
-- eHash 边缘哈希(同主体异背景检测:Canny 边缘密度指纹,
-- 纯色背景不产生边缘,换背景色不影响指纹)
-- ============================================================

ALTER TABLE t_base_file ADD COLUMN IF NOT EXISTS ehash VARCHAR(16);

CREATE INDEX IF NOT EXISTS idx_base_file_ehash
    ON t_base_file (ehash)
    WHERE ehash IS NOT NULL AND is_deleted = '0';
