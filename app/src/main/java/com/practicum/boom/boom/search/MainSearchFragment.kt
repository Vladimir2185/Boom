package com.practicum.boom.boom.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.practicum.boom.MainViewModel
import com.practicum.boom.R
import com.practicum.boom.boom.home.BaseProductAdapter
import kotlinx.android.synthetic.main.main_search_fragment.*


class MainSearchFragment() : Fragment() {
    protected val mainViewModel: MainViewModel by activityViewModels()
    private val NUMBER_OF_PROMO = 0
    companion object {
        @JvmStatic
        fun newInstance() = MainSearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(recycler_mainSearchFragment) {
            val productAdapter = TypeAdapter(requireActivity(), NUMBER_OF_PROMO)
            adapter = productAdapter

            mainViewModel.getListOfProductsByType("scarves").observe(viewLifecycleOwner) {
                productAdapter.productList = it
            }
        }
    }
}