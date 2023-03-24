package com.delixha.githubuser.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.delixha.githubuser.R
import com.delixha.githubuser.data.entity.User
import com.delixha.githubuser.data.remote.response.ItemsItem
import com.delixha.githubuser.databinding.FragmentHomeBinding
import com.delixha.githubuser.ui.adapter.ListUserAdapter
import com.delixha.githubuser.ui.detail.DetailFragment
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

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

        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.github_users_search)

        binding.rvUsers.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(context)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        homeViewModel.listUser.observe(viewLifecycleOwner) { user ->
            setUserData(user)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
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
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                homeViewModel.findUser(newText)
                return false
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
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
        binding.rvUsers.adapter = listUserAdapter

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
}