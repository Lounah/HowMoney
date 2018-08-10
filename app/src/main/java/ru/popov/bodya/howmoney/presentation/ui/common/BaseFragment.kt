package ru.popov.bodya.howmoney.presentation.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.popov.bodya.core.mvp.AppFragment

abstract class BaseFragment : AppFragment() {

    abstract fun setUpToolbarTitle(resId : Int)
    abstract val TAG : String
    abstract val layoutRes : Int
    abstract val toolbarTitleId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View?
            = inflater.inflate(layoutRes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbarTitle(toolbarTitleId)
    }
}