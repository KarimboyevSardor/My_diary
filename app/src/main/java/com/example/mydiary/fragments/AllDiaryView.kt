package com.example.mydiary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.example.mydiary.R
import com.example.mydiary.adapters.DiaryAdapter
import com.example.mydiary.adapters.FolderAdapter
import com.example.mydiary.databinding.FragmentAllDiaryViewBinding
import com.example.mydiary.models.Diary
import com.example.mydiary.models.Folder
import com.example.mydiary.objects.MyObject.diaryList
import com.example.mydiary.objects.MyObject.folderNameList
import com.example.mydiary.objects.MyObject.getViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AllDiaryView : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var folderAdapter: FolderAdapter
    lateinit var diaryAdapter: DiaryAdapter
    private var binding: FragmentAllDiaryViewBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAllDiaryViewBinding.inflate(inflater, container, false)

        binding!!.apply {
            var folderName = "All"
            val viewModel = getViewModel(requireActivity())
            diaryAdapter = DiaryAdapter(diaryList, object : DiaryAdapter.onClick{
                override fun onItemClick(diary: Diary) {
                    val fm = AddEditDiary()
                    val bundle = Bundle()
                    bundle.putParcelable("diary", diary)
                    fm.arguments = bundle
                    findNavController().navigate(R.id.action_main_to_addEditDiary, bundle)
                }
            })

            diaryRec.adapter = diaryAdapter

            folderAdapter = FolderAdapter(folderNameList, object : FolderAdapter.onClick{
                override fun onItemClick(folder: Folder) {
                    folderName = folder.name.toString()
                    viewModel.getDiary()!!.observe(requireActivity()){
                        diaryAdapter.filter(it.filter { it.pack_name == folder.name } as MutableList<Diary>)
                    }
                }
            })
            folderRec.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            folderRec.adapter = folderAdapter
            viewModel.getDiary()!!.observe(requireActivity()) {
                diaryAdapter.filter(it.filter { it.pack_name == folderName } as MutableList<Diary>)
            }
            viewModel.getPackName()!!.observe(requireActivity()) {
                folderAdapter.filter(it)
            }
            folderBtn.setOnClickListener {
                findNavController().navigate(R.id.action_main_to_folders)
            }
            addDiaryBtn.setOnClickListener{
                val fm = AddEditDiary()
                val bundle = Bundle()
                bundle.putString("pack", folderName)
                fm.arguments = bundle
                findNavController().navigate(R.id.action_main_to_addEditDiary, bundle)

            }
        }

        return binding!!.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllDiaryView().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}