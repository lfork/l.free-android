/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lfork.a98620.lfree.main.index;

/**
 * Defines the navigation actions that can be called from the Details screen.
 * 现在先不忙优化，先把im写完先
 */
public interface IndexFragmentNavigator {

    void openSearchActivity();

    void openWebClient(String url);
    //ppt文档心得体会 源码
    // 数据库 配置文件
    // 需求文档
    // 概要设计

}
