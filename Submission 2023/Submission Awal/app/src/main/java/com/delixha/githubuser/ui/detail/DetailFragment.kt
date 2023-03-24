package com.delixha.githubuser.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.delixha.githubuser.R
import com.delixha.githubuser.data.remote.response.DetailUserResponse
import com.delixha.githubuser.databinding.FragmentDetailBinding
import com.delixha.githubuser.utils.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var username = ""

        if (arguments != null) {
            username = arguments?.getString(EXTRA_USERNAME).toString()
            detailViewModel.getDetailUser(username)
        }

        (activity as AppCompatActivity).supportActionBar?.title = username

        val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity())
        sectionsPagerAdapter.username = username
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        (activity as AppCompatActivity).supportActionBar?.elevation = 0f


        detailViewModel.detailUser.observe(viewLifecycleOwner) { user ->
            setUserDetailData(user)
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUserDetailData(user: DetailUserResponse) {
        Glide.with(binding.root)
            .load("https://github.githubassets.com/images/modules/site/home/globe.jpg")
            .into(binding.background)
        Glide.with(binding.root)
            .load(user.avatarUrl)
            .into(binding.avatarUser)
        if (user.name != null) {
            binding.tvName.text = user.name
        } else {
            binding.tvName.text = user.login
        }
        binding.tvName.visibility = View.VISIBLE
        if (user.bio != null) {
            binding.tvBio.text = user.bio
            binding.tvBio.visibility = View.VISIBLE
        } else {
            binding.tvBio.visibility = View.GONE
        }
        if (user.company != null) {
            binding.tvCompany.visibility = View.VISIBLE
            binding.tvCompanyUser.text = user.company
            binding.tvCompanyUser.visibility = View.VISIBLE
        } else {
            binding.tvCompany.visibility = View.GONE
            binding.tvCompanyUser.visibility = View.GONE
        }
        if (user.location != null) {
            binding.tvLocation.visibility = View.VISIBLE
            binding.tvLocationUser.text = user.location
            binding.tvLocationUser.visibility = View.VISIBLE
        } else {
            binding.tvLocation.visibility = View.GONE
            binding.tvLocationUser.visibility = View.GONE
        }
        binding.tvRepository.visibility = View.VISIBLE
        binding.tvRepositoryUser.text = user.publicRepos.toString()
        binding.tvRepositoryUser.visibility = View.VISIBLE
        binding.tvFollowers.visibility = View.VISIBLE
        binding.tvFollowersUser.text = user.followers.toString()
        binding.tvFollowersUser.visibility = View.VISIBLE
        binding.tvFollowing.visibility = View.VISIBLE
        binding.tvFollowingUser.text = user.following.toString()
        binding.tvFollowingUser.visibility = View.VISIBLE
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        var EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
