package com.example.stock

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(), CoroutineScope {

    lateinit var mJob: Job
    private var mIsFirstEnter = true
    private var mIsVisibleToUser = true

    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mJob = Job()
        initData()
        initView()
    }

    override fun onResume() {
        super.onResume()
        if (mIsFirstEnter) {
            mIsFirstEnter = false
            onFirstVisible()
        } else if (mIsVisibleToUser) onVisible()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mIsVisibleToUser = !hidden
        if (mIsVisibleToUser) onVisible()
    }

    open fun onFirstVisible() {}

    open fun onVisible() {}

    //初始化设置LiveModel的监听
    open fun initData() {}

    abstract fun initView()

    fun showToast(content: String?) {
        if (content.isNullOrEmpty()) return
        Toast.makeText(requireContext(), content, Toast.LENGTH_SHORT).show()
    }

    fun showToast(resId: Int) {
        Toast.makeText(requireContext(), resId, Toast.LENGTH_SHORT).show()
    }

    fun logInfo(content: String?) {
        Log.i("xxxx", "${javaClass.simpleName}:$content")
    }

    protected fun replaceFragmentFullScreen(fragment: BaseFragment) {
        if (activity == null) {
            logInfo("activity null")
            return
        }
        val transition = childFragmentManager.beginTransaction()
        transition.replace(R.id.flRootFullScreen, fragment)
            .addToBackStack(fragment.javaClass.name)
            .commit()
    }
}