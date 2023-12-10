package com.example.mydiary.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.example.mydiary.R
import com.example.mydiary.adapters.FoldersAdapter
import com.example.mydiary.databinding.AddFolerDialogBinding
import com.example.mydiary.databinding.FragmentFoldersBinding
import com.example.mydiary.db.MyDb
import com.example.mydiary.models.Folder
import com.example.mydiary.objects.MyObject.folderNameList
import com.example.mydiary.objects.MyObject.getViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Folders : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentFoldersBinding? = null
    lateinit var foldersAdapter: FoldersAdapter
    lateinit var myDb: MyDb
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFoldersBinding.inflate(inflater, container, false)

        binding!!.apply {
            val viewModel = getViewModel(requireActivity())
            val activity = activity as AppCompatActivity
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            setHasOptionsMenu(true)
            foldersAdapter = FoldersAdapter(folderNameList.filter { it.deleted == 0 } as MutableList<Folder>, object : FoldersAdapter.ItemOnClick{
                override fun onClickItem(folder: Folder) {
                    viewModel.addFolderName(folder.name.toString())
                    findNavController().navigateUp()
                }
            })
            foldersRec.adapter = foldersAdapter
            addFolderTbn.setOnClickListener {
                setDialog()
            }
            viewModel.getPackName()!!.observe(requireActivity()) {
                foldersAdapter.filter(it.filter { it.deleted == 0 } as MutableList<Folder>)
            }

        }

        return binding!!.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.folders_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
            }
            R.id.delete_btn -> {
                findNavController().navigate(R.id.action_folders_to_delete)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDialog() {
        val viewModel = getViewModel(requireActivity())
        val myDb = MyDb(requireContext())
        val dialog = Dialog(requireContext())
        val dialogBinding = AddFolerDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.apply {
            closeBtn.setOnClickListener {
                dialog.dismiss()
            }
            addBtn.setOnClickListener {
                val name = addFolderEt.text.toString()
                if (name.isNotEmpty()) {
                    if (folderNameList.filter { it.name == name }.isNotEmpty())  {
                        if (folderNameList.filter { it.name == name }.filter { it.deleted == 1 }.isNotEmpty()) {
                            myDb.updatePack(Folder(name = name, deleted = 0), name)
                            folderNameList = myDb.getPack()
                            viewModel.folderNameLiveData!!.value = folderNameList
                            dialog.dismiss()
                        } else if (folderNameList.filter { it.name == name }.filter { it.deleted == 0 }.isNotEmpty()){
                            Toast.makeText(requireContext(), "There is a folder with this name", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        myDb.addPack(Folder(name = name, deleted = 0))
                        folderNameList.add(Folder(name = name, deleted = 0))
                        viewModel.folderNameLiveData!!.value = folderNameList
                        dialog.dismiss()
                    }
                } else {
                    Toast.makeText(requireContext(), "The name is incomplete", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Folders().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}