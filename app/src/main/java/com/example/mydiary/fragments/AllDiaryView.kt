package com.example.mydiary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.VISIBLE
import com.example.mydiary.R
import com.example.mydiary.adapters.DiaryAdapter
import com.example.mydiary.adapters.FolderAdapter
import com.example.mydiary.databinding.FragmentAllDiaryViewBinding
import com.example.mydiary.db.MyDb
import com.example.mydiary.models.Diary
import com.example.mydiary.models.Folder
import com.example.mydiary.objects.MyObject.diaryList
import com.example.mydiary.objects.MyObject.folderNameList
import com.example.mydiary.objects.MyObject.getViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AllDiaryView : Fragment() {
    private var param1: String? = "All"
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    lateinit var folderAdapter: FolderAdapter
    lateinit var diaryAdapter: DiaryAdapter
    private var binding: FragmentAllDiaryViewBinding? = null
    lateinit var myDb: MyDb
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAllDiaryViewBinding.inflate(inflater, container, false)

        binding!!.apply {
            var folderName = param1
            myDb = MyDb(requireContext())
            val viewModel = getViewModel(requireActivity())
            if (viewModel.getFolderName()!!.value != null) {
                folderName = viewModel.getFolderName()!!.value
            } else {
                folderName = "All"
            }
            diaryAdapter = DiaryAdapter(diaryList.filter { it.deleted == 0 } as MutableList<Diary>, object : DiaryAdapter.onClick{
                override fun onItemClick(diary: Diary) {
                    val fm = AddEditDiary()
                    val bundle = Bundle()
                    bundle.putParcelable("diary", diary)
                    bundle.putString("pack", diary.pack_name)
                    fm.arguments = bundle
                    findNavController().navigate(R.id.action_main_to_addEditDiary, bundle)
                }
            })
            viewModel.getFolderName()!!.observe(requireActivity()) {it1->
                if (it1 != "All") {
                    diaryAdapter.filter(diaryList.filter { it.pack_name == it1 && it.deleted == 0} as MutableList<Diary>)
                } else {
                    diaryAdapter.filter(diaryList.filter { it.deleted == 0 } as MutableList<Diary>)
                }
                folderName = it1
            }
            diaryRec.adapter = diaryAdapter

            folderAdapter = FolderAdapter(folderNameList.filter { it.deleted == 0 } as MutableList<Folder>, object : FolderAdapter.onClick{
                override fun onItemClick(folder: Folder) {
                    folderName = folder.name.toString()
                    viewModel.getDiary()!!.observe(requireActivity()) {
                        if (folderName != "All") {
                            deleteFolerBtn.visibility = View.VISIBLE
                            diaryAdapter.filter(it.filter { it.pack_name == folder.name && it.deleted == 0} as MutableList<Diary>)
                        } else {
                            deleteFolerBtn.visibility = View.INVISIBLE
                            diaryAdapter.filter(it.filter { it.deleted == 0 } as MutableList<Diary>)
                        }
                    }
                }
            })
            deleteFolerBtn.visibility = View.INVISIBLE
            folderRec.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            folderRec.adapter = folderAdapter
            if (folderName != "All") {
                viewModel.getDiary()!!.observe(requireActivity()) { it ->
                    diaryAdapter.filter(it.filter { it.pack_name == folderName && it.deleted == 0 } as MutableList<Diary>)
                }
            } else {
                viewModel.getDiary()!!.observe(requireActivity()) { it ->
                    diaryAdapter.filter(it.filter { it.deleted == 0 } as MutableList<Diary>)
                }
            }

            viewModel.getPackName()!!.observe(requireActivity()) {
                folderAdapter.filter(it.filter { it.deleted == 0 } as MutableList<Folder>)
            }
            folderBtn.setOnClickListener {
                findNavController().navigate(R.id.action_main_to_folders)
            }
            addDiaryBtn.setOnClickListener{
                val fm = AddEditDiary()
                val bundle = Bundle()
                bundle.putString("pack", folderName)
                bundle.putParcelable("diary", Diary())
                fm.arguments = bundle
                findNavController().navigate(R.id.action_main_to_addEditDiary, bundle)

            }
            searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val filtList = mutableListOf<Diary>()
                    for (i in 0 until diaryList.filter { it.deleted == 0 }.size) {
                        if (diaryList.filter { it.deleted == 0 }[i].name!!.toLowerCase().contains(query!!.toLowerCase()) || diaryList.filter { it.deleted == 0}[i].about!!.toLowerCase().contains(query.toLowerCase())) {
                            filtList.add(diaryList.filter { it.deleted == 0 }[i])
                        }
                    }
                    if (query != "") {
                        diaryAdapter.filter(filtList)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val filtList = mutableListOf<Diary>()
                    for (i in 0 until diaryList.filter { it.deleted == 0 }.size) {
                        if (diaryList.filter { it.deleted == 0 }[i].name!!.toLowerCase().contains(newText!!.toLowerCase()) || diaryList.filter { it.deleted == 0 }[i].about!!.toLowerCase().contains(newText.toLowerCase())) {
                            filtList.add(diaryList.filter { it.deleted == 0 }[i])
                        }
                    }
                    if (newText != "") {
                        diaryAdapter.filter(filtList)
                    }
                    return false
                }
            })
            deleteFolerBtn.setOnClickListener {
                deleteFoler(folderName!!)
            }
        }

        return binding!!.root
    }

    private fun deleteFoler(name: String) {
        val viewModel = getViewModel(requireActivity())
        val myDb = MyDb(requireContext())
        myDb.deletePack(Folder(name = name, deleted = 1))
        diaryList = myDb.getDiary()
        viewModel.diaryLiveData!!.value = diaryList
        folderNameList = myDb.getPack()
        viewModel.folderNameLiveData!!.value = folderNameList
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            AllDiaryView().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}