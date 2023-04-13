package com.delixha.githubuser.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.delixha.githubuser.R
import com.delixha.githubuser.data.local.entity.UserEntity
import com.delixha.githubuser.data.remote.response.ItemsItem
import com.delixha.githubuser.databinding.FragmentHomeBinding
import com.delixha.githubuser.ui.adapter.UserAdapter
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvUsers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            val itemDecoration =
                DividerItemDecoration(context, LinearLayoutManager(context).orientation)
            addItemDecoration(itemDecoration)
        }

        homeViewModel.listUser.observe(viewLifecycleOwner) { user ->
            setUserData(user)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeViewModel.isEmpty.observe(viewLifecycleOwner) {
            userNotFound(it)
        }

        homeViewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    binding.root,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                homeViewModel.findUser(query)
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        binding.ivSetting.setOnClickListener(this)
        binding.ivFavorite.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_setting -> {
                findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
            }
            R.id.iv_favorite -> {
                findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        binding.rvUsers.adapter = userAdapter

        userAdapter.setOnItemClickCallback(object :
            UserAdapter.OnItemClickCallback {
            override fun onItemClicked(username: String) {
                val toDetailFragment = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
                toDetailFragment.username = username
                findNavController().navigate(toDetailFragment)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun userNotFound(state: Boolean) {
        binding.tvUserNotFound.visibility = if (state) View.VISIBLE else View.GONE
    }
}