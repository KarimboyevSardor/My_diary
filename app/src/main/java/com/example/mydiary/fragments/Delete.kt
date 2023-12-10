package com.example.mydiary.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.mydiary.R
import com.example.mydiary.adapters.DeleteDiaryAdapter
import com.example.mydiary.databinding.FragmentDeleteBinding
import com.example.mydiary.db.MyDb
import com.example.mydiary.models.Diary
import com.example.mydiary.models.Folder
import com.example.mydiary.objects.MyObject.diaryList
import com.example.mydiary.objects.MyObject.folderNameList
import com.example.mydiary.objects.MyObject.getViewModel
import com.example.mydiary.viewmodel.MyViewModel
import java.util.Calendar

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Delete : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentDeleteBinding? = null
    lateinit var deleteDiaryAdapter: DeleteDiaryAdapter
    lateinit var viewModel: MyViewModel
    private var deleteDiaryList = mutableListOf<Diary>()
    lateinit var myDb: MyDb
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDeleteBinding.inflate(inflater, container, false)

        binding!!.apply {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minut = calendar.get(Calendar.MINUTE)
            val second = calendar.get(Calendar.SECOND)
            val time1 = "$year.$month.$day | $hour:$minut:$second"
            myDb = MyDb(requireContext())
            val activity = activity as AppCompatActivity
            viewModel = getViewModel(requireActivity())
            activity.setSupportActionBar(toolbar)
            setHasOptionsMenu(true)
            activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            deleteDiaryAdapter = DeleteDiaryAdapter(diaryList.filter { it.deleted == 1 } as MutableList<Diary>, object : DeleteDiaryAdapter.OnItemClick{
                override fun onItemClick(diaryList: MutableList<Diary>) {
                    deleteDiaryList = diaryList
                }
            }, requireActivity())
            deleteDiaryRec.adapter = deleteDiaryAdapter
            viewModel.getDiary()!!.observe(requireActivity()) { it ->
                deleteDiaryAdapter.filter(it.filter { it.deleted == 1 } as MutableList<Diary>)
            }
            delete.setOnClickListener {
                if (deleteDiaryList.isNotEmpty()) {
                    for (i in 0 until deleteDiaryList.size) {
                        myDb.deletedDiary(deleteDiaryList[i])
                    }
                    deleteDiaryList.removeAll(deleteDiaryList)
                    diaryList = myDb.getDiary()
                    viewModel.diaryLiveData!!.value = diaryList
                    if (deleteDiaryList.isEmpty()) {
                        viewModel.addChooseDeleteItem(false)
                    }
                }
                Log.d("Delete Diary", "${deleteDiaryList.size}")
            }
            deleteReturn.setOnClickListener {
                if (deleteDiaryList.isNotEmpty()) {
                    for (i in 0 until deleteDiaryList.size) {
                        deleteDiaryList[i].deleted = 0
                        deleteDiaryList[i].deleted_time = time1
                        myDb.updateDiary(deleteDiaryList[i])
                        myDb.updatePack(Folder(name = deleteDiaryList[i].pack_name, deleted = 0), deleteDiaryList[i].pack_name!!)
                    }
                    deleteDiaryList.removeAll(deleteDiaryList)
                    diaryList = myDb.getDiary()
                    viewModel.diaryLiveData!!.value = diaryList
                    folderNameList = myDb.getPack()
                    viewModel.folderNameLiveData!!.value = folderNameList
                    if (deleteDiaryList.isEmpty()) {
                        viewModel.addChooseDeleteItem(false)
                    }
                }
            }
        }

        return binding?.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.exit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Delete().apply {
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