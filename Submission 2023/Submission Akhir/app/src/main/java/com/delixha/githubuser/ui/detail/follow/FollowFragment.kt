package com.delixha.githubuser.ui.detail.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.delixha.githubuser.data.local.entity.UserEntity
import com.delixha.githubuser.data.remote.response.ItemsItem
import com.delixha.githubuser.databinding.FragmentFollowBinding
import com.delixha.githubuser.ui.adapter.UserAdapter
import com.google.android.material.snackbar.Snackbar

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val followViewModel by viewModels<FollowViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var position = 0
        var username = arguments?.getString(ARG_USERNAME)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        if (position == 1) {
            followViewModel.getFollowersUser(username.toString())
        } else {
            followViewModel.getFollowingUser(username.toString())
        }

        binding.rvFollow.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            val itemDecoration =
                DividerItemDecoration(context, LinearLayoutManager(context).orientation)
            addItemDecoration(itemDecoration)
        }

        followViewModel.listUser.observe(viewLifecycleOwner) { user ->
            setUserData(user)
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        followViewModel.isEmpty.observe(viewLifecycleOwner) {
            userNotFound(it)
        }

        followViewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    binding.root,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.rvFollow.minimumHeight = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun userNotFound(state: Boolean) {
        binding.tvUserNotFound.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun setUserData(users: List<ItemsItem>) {
        val listUserEntity = ArrayList<UserEntity>()
        for (user in users) {
            val usr = UserEntity(
                user.login,
                user.avatarUrl,
            )
            listUserEntity.add(usr)
        }

        val userAdapter = UserAdapter(listUserEntity)
        binding.rvFollow.adapter = userAdapter

        userAdapter.setOnItemClickCallback(object :
            UserAdapter.OnItemClickCallback {
            override fun onItemClicked(username: String) {
                val toDetailFragment =
                    FollowFragmentDirections.actionFollowFragmentToDetailFragment()
                toDetailFragment.username = username
                findNavController().navigate(toDetailFragment)
            }
        })
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}