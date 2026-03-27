-- ============================================================
-- 为 T_BASE_FILE 表添加图片相似度向量字段
-- 支持多种算法: ORB (256维) 和 CLIP (512维)
-- ============================================================
-- 前提条件: PostgreSQL 已安装 pgvector 扩展
-- CREATE EXTENSION IF NOT EXISTS vector;

-- 添加向量相关字段
ALTER TABLE t_base_file
    ADD COLUMN IF NOT EXISTS vector_dim INT,
    ADD COLUMN IF NOT EXISTS embedding_vector TEXT,
    ADD COLUMN IF NOT EXISTS image_description VARCHAR(512),
    ADD COLUMN IF NOT EXISTS group_name VARCHAR(255),
    ADD COLUMN IF NOT EXISTS similarity_status VARCHAR(32) DEFAULT 'pending',
    ADD COLUMN IF NOT EXISTS similarity_error_msg TEXT;

-- ORB 向量索引 (256维, L2距离)
-- CREATE INDEX IF NOT EXISTS idx_base_file_orb_vector_hnsw
--     ON t_base_file USING hnsw ((embedding_vector::vector(256)) vector_l2_ops)
--     WHERE embedding_vector IS NOT NULL AND vector_dim = 256;

CREATE EXTENSION IF NOT EXISTS vector;

\d t_base_file

ALTER TABLE t_base_file ALTER COLUMN embedding_vector TYPE vector(512);
ALTER TABLE t_base_file ALTER COLUMN embedding_vector TYPE text;

-- CLIP 向量索引 (512维, 余弦相似度)
-- pgvector 不支持动态维度切换，需要根据实际维度创建索引
-- CLIP 向量索引 (512维, 余弦距离)
CREATE INDEX IF NOT EXISTS idx_base_file_clip_vector_hnsw
    ON t_base_file 
    USING hnsw (((embedding_vector::vector)::vector) vector_cosine_ops)
    WHERE embedding_vector IS NOT NULL AND vector_dim = 512;


CREATE INDEX IF NOT EXISTS idx_base_file_clip_vector_hnsw
    ON t_base_file
        USING hnsw (((embedding_vector::vector)::vector) vector_cosine_ops)
    WHERE embedding_vector IS NOT NULL AND vector_dim = 512;

-- 分组索引
CREATE INDEX IF NOT EXISTS idx_base_file_group_name
    ON t_base_file (group_name)
    WHERE group_name IS NOT NULL;

-- 状态索引
CREATE INDEX IF NOT EXISTS idx_base_file_similarity_status
    ON t_base_file (similarity_status)
    WHERE similarity_status IS NOT NULL;
