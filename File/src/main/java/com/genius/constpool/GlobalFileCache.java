package com.genius.constpool;



import com.genius.cache.FileCache;

import java.util.List;

/**
 * @author Genius
 * @date 2023/04/25 23:03
 **/

/**
 * 全局文件缓存池，用于存放全局文件缓存，便于跨模块调用
 */
public class GlobalFileCache {


    public static List<FileCache> fileCaches
            = List.of();
}
