package com.joaquim.joaquim_teste.ui.home

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.joaquim.joaquim_teste.MyApplication.Companion.globalContext
import com.joaquim.joaquim_teste.R
import com.joaquim.joaquim_teste.data.commom.CheckPermissions
import com.joaquim.joaquim_teste.data.commom.SetToastMessage
import com.joaquim.joaquim_teste.data.commom.VerifyNetwork
import com.joaquim.joaquim_teste.data.commom.extensions.hide
import com.joaquim.joaquim_teste.data.commom.extensions.navigateSafe
import com.joaquim.joaquim_teste.data.commom.extensions.show
import com.joaquim.joaquim_teste.data.commom.extensions.toDate
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetails
import com.joaquim.joaquim_teste.data.model.event.LocalObjectBoxDbEventDetailsItem
import com.joaquim.joaquim_teste.databinding.FragmentHomeBinding
import com.joaquim.joaquim_teste.ui.HomeEventViewModel
import com.joaquim.joaquim_teste.ui.home.adapter.HomeEventsAdapter
import io.objectbox.annotation.NameInDb
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeEventViewModel by sharedViewModel()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeEventsRecyclerView: RecyclerView
    private lateinit var homeLoadingLottieAnimation: LottieAnimationView
    private lateinit var homeNoWifiLottieAnimation: LottieAnimationView

    private val verifyNetwork: VerifyNetwork by inject()
    private val toastMessage: SetToastMessage by inject()
    private val checkPermissions: CheckPermissions by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupBindingVariables()
        setupViews()

        return binding.root
    }

    private fun setupBindingVariables() {
        with(binding) {
            homeEventsRecyclerView = fragmentHomeEventRecyclerView
            homeLoadingLottieAnimation = fragmentHomeEventLoadingAnimation
            homeNoWifiLottieAnimation = fragmentHomeEventNoWifiAnimation
        }
    }

    private fun setupViews() {

        homeViewModel.searchEventsData()
        homeEventsRecyclerView.layoutManager = LinearLayoutManager(this.context)

        with(verifyNetwork) {

            performActionIfConnected {
                observeEventsInfo()
            }

            performActionIfIsNOTConnected {
                homeLoadingLottieAnimation.hide()
                homeNoWifiLottieAnimation.show()
                toastMessage.setToastMessage(R.string.no_network_error)
            }
        }

    }

    private fun observeEventsInfo() {
        val mock1 = LocalObjectBoxDbEventDetailsItem(
            eventDetailDate = "1534784400000",
            eventDetailTitle = "Feira de Produtos Naturais e Orgânicos",
            eventDetailLat = "-30.037878",
            eventDetailLng = "-51.2148497",
            eventDetailDescription = "Toda quarta-feira, das 17h às 22h, encontre a feira mais charmosa de produtos frescos, naturais e orgânicos no estacionamento do Shopping. Sintonizado com a tendência crescente de busca pela alimentação saudável, consumo consciente e qualidade de vida. \\n\\nAs barracas terão grande variedade de produtos, como o shiitake cultivado em Ibiporã há mais de 20 anos, um sucesso na mesa dos que não abrem mão do saudável cogumelo na dieta. Ou os laticínios orgânicos da Estância Baobá, famosos pelo qualidade e modo de fabricação sustentável na vizinha Jaguapitã. Também estarão na feira as conhecidas compotas e patês tradicionais da Pousada Marabú, de Rolândia.\\n\\nA feira do Catuaí é uma nova opção de compras de produtos que não são facilmente encontrados no varejo tradicional, além de ótima pedida para o descanso de final de tarde em família e entre amigos. E com o diferencial de ser noturna, facilitando a vida dos consumidores que poderão sair do trabalho e ir direto para a “Vila Verde”, onde será possível degustar delícias saudáveis nos bistrôs, ouvir música ao vivo, levar as crianças para a diversão em uma estação de brinquedos e relaxar ao ar livre.\\n\\nEXPOSITORES DA VILA VERDE CATUAÍ\\n\\nCraft Hamburgueria\\nNido Pastíficio\\nSabor e Saúde\\nTerra Planta\\nEmpório da Papinha\\nEmpório Sabor da Serra\\nBoleria Dom Leonardi\\nCoisas que te ajudam a viver\\nPatês da Marisa\\nMarabú\\nBaobá\\nAkko\\nCervejaria Amadeus\\n12 Tribos\\nParr Kitchen\\nHorta Fazenda São Virgílio\\nHorta Chácara Santo Antonio\\nSur Empanadas\\nFit & Sweet\\nSK e T Cogumelos\\nDos Quintana\\n\\nLocal: Estacionamento (entrada principal do Catuaí Shopping Londrina)\\n\\n\\nAcesso à Feira gratuito.",
            eventDetailPrice = "19",
            eventDetailUID = "$1231",
            eventDetailImage = "https://i2.wp.com/assentopublico.com.br/wp-content/uploads/2017/07/Feira-de-alimentos-org%C3%A2nicos-naturais-e-artesanais-ganha-um-novo-espa%C3%A7o-em-Ribeir%C3%A3o.jpg"
        )

        val mock2 = LocalObjectBoxDbEventDetailsItem(
            eventDetailDate = "1534784400000",
            eventDetailTitle = "Hackathon Social Woop Sicredi",
            eventDetailLat = "-30.037878",
            eventDetailLng = "-51.2148497",
            eventDetailDescription = "Uma maratona de programação, na qual estudantes e profissionais das áreas de DESIGN, PROGRAMAÇÃO e MARKETING se reunirão para criar projetos com impacto social positivo através dos pilares de Educação Financeira e Colaborar para Transformar.\\n\\nO evento será realizado por duas empresas que são movidas pela transformação: o Woop Sicredi e a Smile Flame.\\n\\n// Pra ficar esperto:\\n\\n- 31/08, 01 e 02 de Setembro, na PUCRS;\\n- 34 horas de duração;\\n- Atividades direcionadas para criação de soluções digitais de impacto social;\\n- Mentorias para apoiar o desenvolvimento das soluções;\\n- Conteúdo de apoio; \\n- Alimentação inclusa;\\n\\n// Programação\\n\\nSexta-feira - 31/08 - 19h ás 22h\\nSábado e Domingo - 01 e 02/09 - 9h do dia 01/09 até as 18h do dia 02/09.\\n\\n// Realização\\nWoop Sicredi\\nSmile Flame\\n\\nMaiores infos em: https://www.hackathonsocial.com.br/\\nTá com dúvida? Manda um e-mail pra gabriel@smileflame.com\\n\\nEaí, ta tão animado quanto nós? Let´s hack!",
            eventDetailPrice = "59.99",
            eventDetailUID = "$1231213",
            eventDetailImage = "https://static.wixstatic.com/media/579ac9_81e9766eaa2741a284e7a7f729429022~mv2.png"
        )

        val mock = LocalObjectBoxDbEventDetails()
        mock.eventDetailsInfos.add(mock1)
        mock.eventDetailsInfos.add(mock2)

        setRecyclerData(mock)
//        homeViewModel.eventsInfo.observe(viewLifecycleOwner, Observer { eventsDetails ->
//
//            eventsDetails?.let {
//                setRecyclerData(it)
//            }
//
//        })
    }

    private fun setRecyclerData(eventDetails: LocalObjectBoxDbEventDetails) {

        val eventsInfo = mutableListOf<LocalObjectBoxDbEventDetailsItem>()

        eventDetails.eventDetailsInfos.forEach { eventDetailsItem ->
            eventsInfo.add(eventDetailsItem)
        }

        val eventsAdapter = HomeEventsAdapter(
            eventsInfo
        ) {
            homeViewModel.setSelectedEventInfo(it)
            navigateToSelectedEvent()
        }

        if (eventsInfo.isNotEmpty()) {
            homeLoadingLottieAnimation.hide()
            homeEventsRecyclerView.show()
            homeEventsRecyclerView.adapter = eventsAdapter
            eventsAdapter.notifyDataSetChanged()
        }

    }

    private fun navigateToSelectedEvent() {
        if (checkPermissions.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            navigateToSelectEvent()
        } else {
            navigateToRequestUserLocationPermissionDialog()
        }
    }

    private fun navigateToRequestUserLocationPermissionDialog() {
        navigateSafe(R.id.action_homeFragment_to_requestLocationPermissionBottomDialog)
    }

    private fun navigateToSelectEvent() {
        navigateSafe(R.id.action_homeFragment_to_eventItemDetailsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}