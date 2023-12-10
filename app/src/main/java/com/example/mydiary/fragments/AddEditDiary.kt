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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.example.mydiary.R
import com.example.mydiary.databinding.FragmentAddEditDiaryBinding
import com.example.mydiary.db.MyDb
import com.example.mydiary.models.Diary
import com.example.mydiary.objects.MyObject.diaryList
import com.example.mydiary.objects.MyObject.getViewModel
import com.example.mydiary.viewmodel.MyViewModel
import java.util.Calendar

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddEditDiary : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var folderName: String? = null
    private var diary: Diary? = null
    lateinit var viewModel: MyViewModel
    private var binding: FragmentAddEditDiaryBinding? = null
    lateinit var time1: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddEditDiaryBinding.inflate(inflater, container, false)
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minut = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        Log.d("AddDiary", "$year.$month.$day | $hour:$minut:$second")
        viewModel = getViewModel(requireActivity())
        binding!!.apply {
            diary = arguments?.getParcelable("diary")!!
            folderName = arguments?.getString("pack")!!
            Log.d("TAG", "onCreateView: $folderName")
            val activity = activity as AppCompatActivity
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            setHasOptionsMenu(true)
            if (diary!!.pack_name!!.isNotEmpty()) {
                aboutEt.setText(diary!!.about)
                nameEt.setText(diary!!.name)
                time.text = diary!!.deleted_time
                time1 = diary!!.deleted_time.toString()
            } else {
                time1 = "$year.$month.$day | $hour:$minut:$second"
                time.text = time1
            }
        }

        return binding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddEditDiary().apply {
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

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (diary!!.pack_name!!.isNotEmpty()) {
            inflater.inflate(R.menu.add_edit_diary_menu, menu)
            if (diary!!.favorite == 0) {
                menu.findItem(R.id.favorite)
                    .setIcon(R.drawable.bookmark_add_fill0_wght400_grad0_opsz24)
            } else {
                menu.findItem(R.id.favorite)
                    .setIcon(R.drawable.bookmark_remove_fill0_wght400_grad0_opsz24)
            }
        } else {
            inflater.inflate(R.menu.add_edit_diary_menu1, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_diary -> {
                addDiary(
                    binding!!.nameEt,
                    binding!!.aboutEt,
                    binding!!.time,
                    folderName!!
                )
                Toast.makeText(requireContext(), "Save diary", Toast.LENGTH_SHORT).show()
                viewModel.addFolderName("All")
                findNavController().navigateUp()
            }
            android.R.id.home -> {
                viewModel.addFolderName(diary!!.pack_name!!)
                findNavController().navigateUp()
            }
            R.id.background -> {
                Toast.makeText(requireContext(), "Background", Toast.LENGTH_SHORT).show()
            }
            R.id.delete -> {
                deleteDiary()
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                viewModel.addFolderName(diary!!.pack_name!!)
                findNavController().navigateUp()
            }
            R.id.save -> {
                updateDiary(
                    binding!!.nameEt,
                    binding!!.aboutEt,
                    binding!!.time,
                    folderName!!
                )
                viewModel.addFolderName(diary!!.pack_name!!)
                findNavController().navigateUp()
            }
            R.id.favorite -> {
                if (diaryList.filter { it.id == diary!!.id }[0].favorite == 1) {
                    item.setIcon(R.drawable.bookmark_add_fill0_wght400_grad0_opsz24)
                    removeFavorite(
                        binding!!.nameEt,
                        binding!!.aboutEt,
                        binding!!.time,
                        folderName!!
                    )
                    Toast.makeText(requireContext(), "Removed favorite", Toast.LENGTH_SHORT).show()
                } else {
                    item.setIcon(R.drawable.bookmark_remove_fill0_wght400_grad0_opsz24)
                    addFavorite(
                        binding!!.nameEt,
                        binding!!.aboutEt,
                        binding!!.time,
                        folderName!!
                    )
                    Toast.makeText(requireContext(), "Added favorite", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        return super.onOptionsItemSelected(item)
    }

    private fun addFavorite(nameEt: EditText, aboutEt: EditText, createTimeTv: TextView, folderName: String) {
        val db = MyDb(requireContext())
        val nameDiary = nameEt.text.toString()
        val aboutDiary = aboutEt.text.toString()
        val timeDiary = createTimeTv.text.toString()
        db.updateDiary(Diary(
            id = diary!!.id,
            name = nameDiary,
            about = aboutDiary,
            deleted = diary!!.deleted,
            favorite = 1,
            pack_name = folderName,
            deleted_time = timeDiary
        ))
        val index = diaryList.indexOf(diaryList.filter { it.id == diary!!.id }[0])
        diaryList[index] = Diary(
            id = diary!!.id,
            name = nameDiary,
            about = aboutDiary,
            deleted = diary!!.deleted,
            favorite = 1,
            pack_name = folderName,
            deleted_time = timeDiary
        )
        viewModel.diaryLiveData!!.value  = diaryList
    }

    private fun removeFavorite(nameEt: EditText, aboutEt: EditText, createTimeTv: TextView, folderName: String) {
        val db = MyDb(requireContext())
        val nameDiary = nameEt.text.toString()
        val aboutDiary = aboutEt.text.toString()
        val timeDiary = createTimeTv.text.toString()
        db.updateDiary(Diary(
            id = diary!!.id,
            name = nameDiary,
            about = aboutDiary,
            deleted = diary!!.deleted,
            favorite = 0,
            pack_name = folderName,
            deleted_time = timeDiary
        ))
        val index = diaryList.indexOf(diaryList.filter { it.id == diary!!.id }[0])
        diaryList[index] = Diary(
            id = diary!!.id,
            name = nameDiary,
            about = aboutDiary,
            deleted = diary!!.deleted,
            favorite = 0,
            pack_name = folderName,
            deleted_time = timeDiary
        )
        viewModel.diaryLiveData!!.value  = diaryList
    }

    private fun deleteDiary() {
        val db = MyDb(requireContext())
        diary!!.deleted = 1
        diary!!.deleted_time = "$time1\nRemaining time: 30"
        db.updateDiary(diary!!)
        diaryList.remove(diary)
        viewModel.diaryLiveData!!.value  = diaryList
    }

    private fun updateDiary(nameEt: EditText, aboutEt: EditText, createTimeTv: TextView, folderName: String) {
        val db = MyDb(requireContext())
        val nameDiary = nameEt.text.toString()
        val aboutDiary = aboutEt.text.toString()
        val timeDiary = createTimeTv.text.toString()
        if (nameDiary.isNotEmpty() || aboutDiary.isNotEmpty()) {
            db.updateDiary(
                Diary(
                    id = diary!!.id,
                    name = nameDiary,
                    about = aboutDiary,
                    deleted = diary!!.deleted,
                    favorite = diaryList.filter { it.id == diary!!.id }[0].favorite,
                    pack_name = folderName,
                    deleted_time = timeDiary
                )
            )
            val index = diaryList.indexOf(diaryList.filter { it.id == diary!!.id }[0])
            diaryList[index] = Diary(
                id = diary!!.id,
                name = nameDiary,
                about = aboutDiary,
                deleted = diary!!.deleted,
                favorite = diary!!.favorite,
                pack_name = folderName,
                deleted_time = timeDiary
            )
            viewModel.diaryLiveData!!.value  = diaryList
        }
    }

    private fun addDiary(name: EditText, about: EditText, time: TextView, packName: String) {
        val db = MyDb(requireContext())
        val nameDiary = name.text.toString()
        val aboutDiary = about.text.toString()
        val timeDiary = time.text.toString()
        if (nameDiary.isNotEmpty() || aboutDiary.isNotEmpty()) {
            val diary = Diary(
                name = nameDiary,
                about = aboutDiary,
                deleted_time = timeDiary,
                deleted = 0,
                favorite = 0,
                pack_name = packName
            )
            diaryList.add(diary)
            db.addDiary(diary)
            viewModel.diaryLiveData!!.value  = diaryList
        }
    }
}