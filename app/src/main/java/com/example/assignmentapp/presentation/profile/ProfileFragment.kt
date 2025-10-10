package com.example.assignmentapp.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.assignmentapp.R
import com.example.assignmentapp.databinding.FragmentProfileBinding
import com.example.assignmentapp.domain.model.User
import com.example.assignmentapp.presentation.auth.login.LoginActivity
import com.example.assignmentapp.presentation.core.BaseFragment
import com.example.assignmentapp.presentation.extention.collectLatestLifeCycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpObservers()
        getUser()
    }


    private fun setUpObservers() {

        this.collectLatestLifeCycleFlow(viewModel.errorMessage) {
            showMessageDialogWithOkAction(it)
        }

        this.collectLatestLifeCycleFlow(viewModel.isLoading) { isLoading ->
            if (isLoading) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        this.collectLatestLifeCycleFlow(viewModel.user) {
            bindDataUI(it)
        }

        this.collectLatestLifeCycleFlow(viewModel.loggedOutSuccessful) {
            navigateToLogin()
        }

        binding.btnLogout.setOnClickListener {
            showMessageDialogWithPositiveAndNegativeAction(messageContent = getString(
                R.string.msg_do_you_want_to_logout
            ), okAction = {
               logout()
            }, okActionText = getString(
                R.string.action_yes,
            ), negativeText = getString(
                R.string.action_cancel
            ), negativeAction = {

            })
        }

    }

    private fun bindDataUI(it: User?) {
        binding.tvEmail.text = it?.email
        binding.tvFirstName.text = it?.firstName
        binding.tvLastName.text = it?.lastName
    }

    private fun getUser() {
        viewModel.getUser()
    }

    private fun logout() {
        viewModel.logout()
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
