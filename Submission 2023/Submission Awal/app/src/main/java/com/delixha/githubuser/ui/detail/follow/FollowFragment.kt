package com.delixha.githubuser.ui.detail.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.delixha.githubuser.R
import com.delixha.githubuser.data.entity.User
import com.delixha.githubuser.data.remote.response.ItemsItem
import com.delixha.githubuser.databinding.FragmentFollowBinding
import com.delixha.githubuser.ui.adapter.ListUserAdapter
import com.delixha.githubuser.ui.detail.DetailFragment

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val followViewModel by viewModels<FollowViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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

        binding.rvFollow.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(context)
        binding.rvFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        followViewModel.listUser.observe(viewLifecycleOwner) { user ->
            setUserData(user)
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        followViewModel.isEmpty.observe(viewLifecycleOwner) {
            userNotFound(it)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun userNotFound(state: Boolean) {
        binding.tvUserNotFound.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun setUserData(users: List<ItemsItem?>) {
        val listUser = ArrayList<User>()
        for (user in users) {
            val usr = User(
                user?.login,
                user?.avatarUrl
            )
            listUser.add(usr)
        }

        val listUserAdapter = ListUserAdapter(listUser)
        binding.rvFollow.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object :
            ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(username: String) {
                val mDetailFragment = DetailFragment()

                val mBundle = Bundle()
                mBundle.putString(DetailFragment.EXTRA_USERNAME, username)

                mDetailFragment.arguments = mBundle

                val mFragmentManager = parentFragmentManager
                mFragmentManager.commit {
                    addToBackStack(null)
                    replace(
                        R.id.frame_contrainer,
                        mDetailFragment,
                        DetailFragment::class.java.simpleName
                    )
                }
            }
        })
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}