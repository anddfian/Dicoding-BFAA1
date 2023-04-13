package com.delixha.githubuser.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.delixha.githubuser.R
import com.delixha.githubuser.data.local.entity.UserEntity
import com.delixha.githubuser.databinding.FragmentFavoriteBinding
import com.delixha.githubuser.ui.adapter.UserAdapter
import com.delixha.githubuser.utils.FavoriteViewModelFactory

class FavoriteFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: FavoriteViewModelFactory =
            FavoriteViewModelFactory.getInstance(requireActivity())
        val favoriteViewModel: FavoriteViewModel by viewModels { factory }

        binding.rvUsers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            val itemDecoration =
                DividerItemDecoration(context, LinearLayoutManager(context).orientation)
            addItemDecoration(itemDecoration)
        }

        favoriteViewModel.getFavoriteUser().observe(viewLifecycleOwner) { favoriteUser ->
            setUserData(favoriteUser)
        }

        binding.ivBack.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                binding.ivBack.findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUserData(users: List<UserEntity>) {
        if (users.isEmpty()) {
            userNotFound(true)
        } else {
            userNotFound(false)
            val listUserEntity = ArrayList<UserEntity>()
            for (user in users) {
                val usr = UserEntity(
                    user.username,
                    user.avatarUrl,
                )
                listUserEntity.add(usr)
            }

            val userAdapter = UserAdapter(listUserEntity)
            binding.rvUsers.adapter = userAdapter

            userAdapter.setOnItemClickCallback(object :
                UserAdapter.OnItemClickCallback {
                override fun onItemClicked(username: String) {
                    val toDetailFragment =
                        FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment()
                    toDetailFragment.username = username
                    findNavController().navigate(toDetailFragment)
                }
            })
        }
    }

    private fun userNotFound(state: Boolean) {
        binding.tvUserNotFound.visibility = if (state) View.VISIBLE else View.GONE
    }
}