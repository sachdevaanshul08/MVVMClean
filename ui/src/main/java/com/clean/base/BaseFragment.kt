package com.clean.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.clean.R
import com.clean.activity.MainActivity
import com.clean.mvvm.viewmodelsfactory.ViewModelFactory
import com.clean.utils.autoCleared
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject


abstract class BaseFragment<Z : ViewModel?, T : ViewDataBinding> : DaggerFragment() {

    var mRootView: View? = null
    var binding by autoCleared<T>()

    lateinit var parentActivity: MainActivity

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppCompatActivity) {
            val activity = context as MainActivity
            this.parentActivity = activity
        }
    }

    protected fun getViewModel(v: Class<Z>): Z {
        return ViewModelProviders.of(this, this.viewModelFactory).get(v)
    }

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * @return title
     */
    @get:StringRes
    abstract val title: Int


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = binding.root
        return mRootView
    }

    /**
     * This function triggers when fragment is visible
     * again after returning from some other fragment
     */
    fun visibleAgain() {
        setTitle(parentActivity.resources?.getString(title))
        parentActivity.showBackButton(parentActivity.isBackButtonRequired())
    }

    override fun onResume() {
        super.onResume()
        visibleAgain()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        //binding.executePendingBindings()
        onViewCreation(savedInstanceState)
    }

    protected fun showProgressBar(boolean: Boolean) {
        parentActivity.showProgressBar(boolean)
    }

    /**
     * Set the title on the toolbar
     *
     * @param title to be set
     */
    private fun setTitle(title: String?) {
        parentActivity.supportActionBar?.setTitle(title)
    }

    abstract fun onViewCreation(savedInstanceState: Bundle?)

    /**
     * Show the snackbar
     *
     * @param view base view
     * @param msg message to be displayed
     */
    fun showSnackBar(view: View?, msg: String?) {
        if (view == null || msg.isNullOrBlank()) return
        val snack = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
        snack.view.setBackgroundColor(ContextCompat.getColor(parentActivity, R.color.colorAccent))
        snack.show()
    }

    protected fun editIdlingResource(increment: Boolean) {
        if (increment) parentActivity.incrementCountingIdlingResource()
        else parentActivity.decrementCountingIdlingResource()
    }


}