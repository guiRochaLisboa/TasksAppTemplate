package com.example.tasks.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.model.TaskModel
import com.example.tasks.viewmodel.TaskFormViewModel
import kotlinx.android.synthetic.main.activity_register.button_save_task
import kotlinx.android.synthetic.main.activity_task_form.*
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var mViewModel: TaskFormViewModel
    private var mTaskId = 0
    private val mListPriorityId: MutableList<Int> = arrayListOf()
    private val mDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        mViewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)

        // Inicializa eventos
        listeners()
        observe()

        mViewModel.listPriorities()

        loadDataFromActivity()
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.button_save_task) {
            handleSave()
        } else if (id == R.id.button_date) {
            showDatePicker()
        }
    }

    private fun listeners() {
        button_save_task.setOnClickListener(this)
        button_date.setOnClickListener(this)
    }


    private fun loadDataFromActivity() {
        val bundle = intent.extras
        if (bundle != null) {
            mTaskId = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            mViewModel.load(mTaskId)
            button_save_task.text = getString(R.string.update_task)
        }
    }


    /**
     * Função handleSave atribui o dados fornecidos pelo usuraio para a
     * task criada e manda o objeto task para nossa TaskFormViewModel
     */

    private fun handleSave() {


        val task = TaskModel().apply {
            this.id = mTaskId
            this.description = edit_description.text.toString()
            this.complete = check_complete.isChecked
            this.dueDate = button_date.text.toString()
            this.priorityId = mListPriorityId[spinner_priority.selectedItemPosition]
        }

        mViewModel.save(task)


    }


    /**
     *  A função showDatePicker tem a função de mostrar o calendario na data atual, igual o "View.OnClickListener"
     *  o "DatePickerDialog.OnDateSetListener" também é uma interface e seus membros deve ser implementados, no caso
     *  "onDateSet" que nada mais que cuida do click do caléndario e atribui o valor para o texto do button_date
     */

    private fun showDatePicker() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, this, year, month, day).show()

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val str = mDateFormat.format(calendar.time)
        button_date.text = str
    }

    private fun observe() {

        mViewModel.priorities.observe(this, androidx.lifecycle.Observer {
            val list: MutableList<String> = arrayListOf()
            for (item in it) {
                list.add(item.description)
                mListPriorityId.add(item.id)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            spinner_priority.adapter = adapter
        })

        mViewModel.validation.observe(this, androidx.lifecycle.Observer {
            if (it.success()) {
                if(mTaskId == 0){
                    toast(getString(R.string.task_created))
                }else{
                    toast(getString(R.string.task_updated))
                }
                finish()
            } else {
                toast(it.faliure())
            }
        })


        mViewModel.task.observe(this, androidx.lifecycle.Observer {
            edit_description.setText(it.description)
            check_complete.isChecked = it.complete

            val date = SimpleDateFormat("yyyy/mm/dd").parse(it.dueDate)
            button_date.text = mDateFormat.format(date)

            spinner_priority.setSelection(getIndex(it.priorityId))
        })

    }

    private fun getIndex(priorityId: Int): Int {
        var index = 0
        for (i in 0 until mListPriorityId.count()) {
            if (mListPriorityId[i] == priorityId) {
                index = i
                break
            }
        }
        return index
    }

    private fun toast(str: String){
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()

    }





}
