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
import androidx.navigation.fragment.findNavController
import com.example.mydiary.R
import com.example.mydiary.databinding.FragmentAddEditDiaryBinding
import com.example.mydiary.db.MyDb
import com.example.mydiary.models.Diary
import com.example.mydiary.objects.MyObject.diaryList

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

    lateinit var folderName: String
    lateinit var diary: Diary
    private var binding: FragmentAddEditDiaryBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddEditDiaryBinding.inflate(inflater, container, false)

        binding!!.apply {
            diary = arguments?.getParcelable<Diary>("diary")!!
            folderName = arguments?.getString("pack")!!
            Log.d("TAG", "onCreateView: $folderName")
            val activity = activity as AppCompatActivity
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            setHasOptionsMenu(true);
            if (diary != null) {
                aboutEt.setText(diary.about)
                nameEt.setText(diary.name)
                createTimeTv.text = diary.deleted_time
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_edit_diary_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.background -> {
                Toast.makeText(requireContext(), "Background", Toast.LENGTH_SHORT).show()
            }
            R.id.delete -> {
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
            }
            R.id.save -> {
                saveDiary(binding!!.nameEt, binding!!.aboutEt, binding!!.createTimeTv, folderName)
                Toast.makeText(requireContext(), "Save", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            R.id.favorite -> {
                Toast.makeText(requireContext(), "Favorite", Toast.LENGTH_SHORT).show()
            }
        }
        
        return super.onOptionsItemSelected(item)
    }

    fun saveDiary(name: EditText, about: EditText, time: TextView, packName: String) {
        val db = MyDb(requireContext())
        val nameDiary = name.text.toString()
        val aboutDiary = about.text.toString()
        val timeDiary = time.text.toString()
        if (nameDiary.isNotEmpty() || aboutDiary.isNotEmpty()) {
            val diary = Diary(name = nameDiary, about = aboutDiary, deleted_time = timeDiary, deleted = 0, favorite = 0, pack_name = packName)
            diaryList.add(diary)
            db.addDiary(diary)
        }
    }
}