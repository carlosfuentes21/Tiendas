package com.wposs.stores

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.wposs.stores.databinding.FragmentEditStoreBinding
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

// TODO: Rename parameter arguments, choose names that match

class EditStoreFragment : Fragment() {

    private lateinit var mBinding: FragmentEditStoreBinding
    private var mActivity: MainActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEditStoreBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = getString(R.string.edit_store_add)

        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mActivity?.onBackPressed()
                true
            }
            R.id.action_save -> {
                val store = StoreEntity(
                    name = mBinding.etName.text.toString().trim(),
                    phone = mBinding.etPhone.text.toString().trim(),
                    website = mBinding.etWebsite.text.toString().trim()
                )

                doAsync {
                    StoreAplication.database.storeDao().addStore(store)
                    uiThread {
                        hideKeyBoard()
                        Snackbar.make(
                            mBinding.root,
                            getString(R.string.edit_store_message_save_success),
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                        mActivity?.onBackPressed()
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        //return super.onOptionsItemSelected(item)
    }

    private fun hideKeyBoard() {
        val vieww = mActivity!!.currentFocus
        if (vieww != null) {
            //Aquí esta la magia
            val input = mActivity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            input.hideSoftInputFromWindow(vieww.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        hideKeyBoard()
        super.onDestroyView()
    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title = getString(R.string.app_name)
        mActivity?.hideFab(true)
        super.onDestroy()
    }

}