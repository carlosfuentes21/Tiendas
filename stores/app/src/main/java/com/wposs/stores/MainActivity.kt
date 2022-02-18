package com.wposs.stores

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wposs.stores.databinding.ActivityMainBinding
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), OnClickListener, MainAux {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.fab.setOnClickListener { launchEditFragment() }

        setupRecyclerView()

    }

    //lanzar fragmento
    private fun launchEditFragment(args: Bundle? = null) {
        val fragment = EditStoreFragment()
        if (fragment != null) {
            fragment.arguments = args
        }
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.containerMain, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        hideFab()
    }

    private fun setupRecyclerView() {
        mAdapter = StoreAdapter(mutableListOf(), this)
        mGridLayout = GridLayoutManager(this, 2)
        getStores()
        mBinding.reciclerView.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

    private fun getStores() {
        doAsync {
            val stores = StoreAplication.database.storeDao().getAllStores()
            uiThread {
                mAdapter.setStores(stores)
            }
        }
    }

    /*
    *OnClickListener
    * */
    override fun onClick(storeId: Long) {
        val args = Bundle()
        args.putLong(getString(R.string.arg_id), storeId)

        launchEditFragment(args)
    }

    override fun onFavoriteStore(storeEntity: StoreEntity) {
        storeEntity.isFavotite = !storeEntity.isFavotite
        doAsync {
            StoreAplication.database.storeDao().updateStore(storeEntity)
            uiThread {
                updateStore(storeEntity)
            }
        }
    }

    override fun onDeleteStore(storeEntity: StoreEntity) {
        val items = arrayOf("Eliminar", "Llamar", "Ir al sitio web")
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_options_title)
            .setItems(items, { dialog, i ->
                when (i) {
                    0 -> confirmDelete(storeEntity)
                    1->Toast.makeText(this, "Llamar...", Toast.LENGTH_SHORT).show()
                    2->Toast.makeText(this, "Sitio web...", Toast.LENGTH_SHORT).show()
                }
            })
            .show()
    }

    private fun confirmDelete(storeEntity: StoreEntity) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_title)
            .setPositiveButton(R.string.dialog_delete_title_confirm, { dialog, which ->
                doAsync {
                    StoreAplication.database.storeDao().deleteStore(storeEntity)
                    uiThread {
                        mAdapter.delete(storeEntity)
                    }
                }
            })
            .setNegativeButton(R.string.dialog_delete_cancel_title, null)
            .show()
    }

    /*
    * MainAux
    * */
    override fun hideFab(isVisible: Boolean) {
        if (isVisible) {
            mBinding.fab.show()
        } else {
            mBinding.fab.hide()
        }
    }

    override fun addStore(storeEntity: StoreEntity) {
        mAdapter.add(storeEntity)
    }

    override fun updateStore(storeEntity: StoreEntity) {
        mAdapter.update(storeEntity)
    }
}