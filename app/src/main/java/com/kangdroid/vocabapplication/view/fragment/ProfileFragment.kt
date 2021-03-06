package com.kangdroid.vocabapplication.view.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kangdroid.vocabapplication.data.entity.user.UserSession
import com.kangdroid.vocabapplication.databinding.FragmentProfileBinding
import com.kangdroid.vocabapplication.recycler.QuestionLogRecyclerAdapter
import com.kangdroid.vocabapplication.view.EditPasswordDialog
import com.kangdroid.vocabapplication.view.LoginActivity
import com.kangdroid.vocabapplication.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment @Inject constructor() : Fragment() {
    private var _fragmentProfileBinding: FragmentProfileBinding? = null
    private val fragmentRegisterBinding: FragmentProfileBinding get() = _fragmentProfileBinding!!

    private val questionLogRecyclerAdapter: QuestionLogRecyclerAdapter by lazy {
        QuestionLogRecyclerAdapter(UserSession.currentUser!!.questionLog)
    }

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return fragmentRegisterBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentRegisterBinding.removeAccountLayout.setOnClickListener {
            showConfirmDialog()
        }

        fragmentRegisterBinding.credentialLayout.setOnClickListener {
            val editDialog: EditPasswordDialog = EditPasswordDialog(requireContext(), profileViewModel.userRepository).apply {
                setCanceledOnTouchOutside(true)
                setCancelable(true)
            }
            editDialog.show()
        }

        // Init observer
        initObserver()

        // Init recycler
        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentProfileBinding = null
    }

    private fun initObserver() {
        profileViewModel.isRemoveSucceed.observe(viewLifecycleOwner) {
            if (it) {
                Log.d(this::class.java.simpleName, "Remove data succeed!")
                val intent: Intent = Intent(requireContext(), LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            } else {
                Log.e(this::class.java.simpleName, "Removing data failed. Please refer to logcat for more details.")
            }
        }
    }

    private fun initRecyclerView() {
        fragmentRegisterBinding.questionRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        fragmentRegisterBinding.questionRecyclerView.adapter = questionLogRecyclerAdapter
    }

    private fun showConfirmDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm remove account?")
            .setMessage("Remove account information and data?\nTHIS CANNOT BE UNDONE!")
            .setPositiveButton("Yes") { _, _ ->
                // Request View Model to remove data
                profileViewModel.removeData()
            }
            .setNegativeButton("No") { _, _ ->
                Log.d(this::class.java.simpleName, "Canceled removing user data!")
            }
            .create()
            .show()
    }
}