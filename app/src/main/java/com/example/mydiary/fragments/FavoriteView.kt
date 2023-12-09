package com.example.mydiary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mydiary.R
import com.example.mydiary.adapters.DiaryAdapter
import com.example.mydiary.databinding.FragmentFavoriteViewBinding
import com.example.mydiary.models.Diary
import com.example.mydiary.objects.MyObject.diaryList
import com.example.mydiary.objects.MyObject.getViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FavoriteView : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentFavoriteViewBinding? = null
    lateinit var diaryAdapter: DiaryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavoriteViewBinding.inflate(inflater, container, false)

        binding!!.apply {
            val viewModel = getViewModel(requireActivity())
            diaryAdapter = DiaryAdapter(diaryList.filter { it.favorite == 1 } as MutableList<Diary>, object : DiaryAdapter.onClick{
                override fun onItemClick(diary: Diary) {
                    val fm = AddEditDiary()
                    val bundle = Bundle()
                    bundle.putParcelable("diary", diary)
                    bundle.putString("pack", diary.pack_name)
                    fm.arguments = bundle
                    findNavController().navigate(R.id.action_main_to_addEditDiary, bundle)
                }
            })
            favoriteDiaryRec.adapter = diaryAdapter
            viewModel.getDiary()!!.observe(requireActivity()) { it ->
                diaryAdapter.filter(it.filter { it.favorite == 1 } as MutableList<Diary>)
            }
        }

        return binding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteView().apply {
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