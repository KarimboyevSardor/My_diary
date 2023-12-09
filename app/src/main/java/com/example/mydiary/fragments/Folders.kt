package com.example.mydiary.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
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
            foldersAdapter = FoldersAdapter(folderNameList, object : FoldersAdapter.ItemOnClick{
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
                foldersAdapter.filter(it)
            }
        }

        return binding!!.root
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
                    if (folderNameList.none { it.name == name }) {
                        myDb.addPack(Folder(name = name, deleted = 0))
                        folderNameList.add(Folder(name = name, deleted = 0))
                        viewModel.folderNameLiveData!!.value = folderNameList
                        Toast.makeText(requireContext(), "Added folder $name", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Such a folder exists", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Please do not leave a space", Toast.LENGTH_SHORT).show()
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