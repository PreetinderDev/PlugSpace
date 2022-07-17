package com.plugspace.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.plugspace.R
import com.plugspace.activities.BaseActivity
import com.plugspace.common.Constants
import com.plugspace.common.Logger
import com.plugspace.common.Preferences
import com.plugspace.common.Utils
import com.plugspace.model.AutoChatModel
import com.plugspace.model.ObjectResponseModel
import com.plugspace.retrofitApi.ApiClient
import com.plugspace.retrofitApi.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AutoChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AutoChatFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        autoChat();
        return inflater.inflate(R.layout.fragment_auto_chat, container, false)
    }

    private fun autoChat() {
        if (Utils.isNetworkAvailable(activity, true, false)) {
         Log.e("auto msgs","called")
            BaseActivity.showProgressDialog(activity)
            val service = ApiClient.UserApiClient(activity, Constants.token).create(
                ApiService::class.java
            )
            val call = service.autoChat()
            call.enqueue(object : Callback<AutoChatModel?> {
                override fun onResponse(
                    call: Call<AutoChatModel?>,
                    response: Response<AutoChatModel?>
                ) {
                    hideProgressDialog(activity);
                    if (response.isSuccessful && response.body() != null) {
                        val model = response.body()
                        if (model!!.success) {
//                            hideProgressDialog(activity);
                           Log.e("auto chat response",model.message.size.toString())
                        } else {

                            if (!model.success) {
                                Log.e("auto chat response","Empty")

                            }
                        }
                    } else {
                        BaseActivity.hideProgressDialog(activity)
                        Utils.showAlert(
                            activity,
                            activity!!.resources.getString(R.string.app_name),
                            activity!!.resources.getString(R.string.something_went_wrong)
                        )
                    }
                }

                override fun onFailure(call: Call<AutoChatModel?>, t: Throwable) {
                    BaseActivity.hideProgressDialog(activity)
                    if (!Utils.isNetworkAvailable(activity, true, false)) {
                        Utils.showAlert(
                            activity, activity!!.resources.getString(R.string.app_name),
                            activity!!.resources.getString(R.string.error_network)
                        )
                    } else {
                        Utils.showAlert(
                            activity, activity!!.resources.getString(R.string.app_name),
                            activity!!.resources.getString(R.string.technical_problem)
                        )
                    }
                    t.printStackTrace()
                }
            })
        }
    }

}