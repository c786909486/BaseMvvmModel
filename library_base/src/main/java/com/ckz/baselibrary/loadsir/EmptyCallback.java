package com.ckz.baselibrary.loadsir;

import com.ckz.library_base.R;
import com.kingja.loadsir.callback.Callback;

/**
 * 应用模块: loadSir
 * <p>
 * 类描述: 空页面
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-01-27
 */
public class EmptyCallback extends Callback
{
    @Override
    protected int onCreateView()
    {
        return R.layout.base_layout_empty;
    }
}
