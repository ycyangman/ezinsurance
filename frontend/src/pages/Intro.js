import React, { Component } from 'react';

import CloseIcon from '@mui/icons-material/Close';
import { Link } from "react-router-dom";

import ApiService from "../common/ApiService";
import '../App.css'

class Intro extends Component{

  constructor(props){
    super(props);

    this.state = {
	  prdcd:'',
	  custNm:'',
	  aclBirdt: '',
	  gndrCd: '',
	  phoneNo:'',
	  joinStyle : {display: 'flex'},
	  data: '',
      message: null
    }
  }
  
  componentDidMount(){
	this.setState({joinStyle: {display: 'none'}})
  }

    handleGender = (e) => {
        e.preventDefault()

		this.setState({
			gndrCd: e.target.value
		});
  
  
        var unboundForEach = Array.prototype.forEach,
			forEach = Function.prototype.call.bind(unboundForEach);
			
		forEach(document.querySelectorAll('div.gender_wrap button'), function (el) {
			
			if (e.target.value === '1') {
				if( el.classList.contains('male') ) {
					el.classList.add('chked');
				}
				if( el.classList.contains('female') ) {
					el.classList.remove('chked');
				}
            }
			else {
				if( el.classList.contains('male') ) {
					el.classList.remove('chked');
				}
				if( el.classList.contains('female') ) {
					el.classList.add('chked');
				}
			}
		});
    }
	
	handleProdcut = (e) =>{
		e.preventDefault();
		//alert(e.target.value);
		
		this.setState({
			prdcd: e.target.value
		});
		

		var unboundForEach = Array.prototype.forEach,
			forEach = Function.prototype.call.bind(unboundForEach);
			
		forEach(document.querySelectorAll('li.list'), function (el) {
			el.classList.remove('on');
		});

		forEach(document.querySelectorAll('li.list'), function (el) {
			//alert(el.classList);
			if (e.target.value === "P00000001" && el.classList.contains('icon01') ) {
				el.classList.add('on');
			}

			if (e.target.value === "P00000002" && el.classList.contains('icon02') ) {
				el.classList.add('on');
			}
			if (e.target.value === "P00000003" && el.classList.contains('icon03') ) {
				el.classList.add('on');
			}
			if (e.target.value === "P00000004" && el.classList.contains('icon04') ) {
				el.classList.add('on');
			}
			if (e.target.value === "P00000005" && el.classList.contains('icon05') ) {
				el.classList.add('on');
			}
			
		});
	
	}

    handleValueChange = (e) => {
 		let nextState = {};
        nextState[e.target.name] = e.target.value;
        this.setState(nextState);
    }
	
    handleFormSubmit = (e) => {
        e.preventDefault()

		if( this.state.prdcd === '' ) {
    
			alert('상품을 선책하세요');
			return;
		  }

		console.log(this.state);
		
		let data = {
			svcId : 'PLA001SVC',
			svcFn : 'calcPrm',

			prdcd: this.state.prdcd,
			custNm: this.state.custNm,
			aclBirdt: this.state.aclBirdt,
			gndrCd: this.state.gndrCd,
			phoneNo: this.state.phoneNo,

		  }

        const url = '/plans/online';

        const config = {
            headers: {
                'content-type': 'application/json'
            }
        }
        ApiService.postTran(url, data, config)
		.then((response) => {

			console.log(response.data.data);
			this.setState({data: response.data.data});

			console.log(this.state.data.prm);

			this.setState({joinStyle: {display: 'flex'}});

		})
		.catch((error) => {
			console.error('There was an error!', error);
			alert(error);
		});
    }
	
  render(){

    return(
	<div className="oneclick_cont">
		<div className="inner">
			<h3 className="h3_tit">보험료 간편조회 서비스</h3>

			<div class="step_box_wrap  icon_box_wrap">
			
				<div class="step_box active">
					<div class="step_info">
						<p class="step_text"><strong>보장선택</strong></p>
					</div>
					<div class="step_con">
						<ul class="oc_product_list">
						<li class="list icon01">
						<button type="button" value="P00000001" title="선택해제됨" onClick={this.handleProdcut}>치아보험 <br/><br/>치료 본인부담률<br/>181.1%</button></li>
						<li class="list icon02"><button type="button" value="P00000002" onClick={this.handleProdcut}>암보험  <br/><br/>한국인의 사망원인<br/>1위 암 27.9%</button></li>
						<li class="list icon03"><button type="button" value="P00000003" onClick={this.handleProdcut}>치매·실버<br/><br/>치매환자 돌봄비용<br/>연간 2,074만원</button></li>
						<li class="list icon04"><button type="button" value="P00000004" onClick={this.handleProdcut}>건강보험 <br/><br/>수술로 인한 진료비<br/>평균 약 321만원</button></li>
						<li class="list icon05"><button type="button" value="P00000005" onClick={this.handleProdcut}>사망정기 <br/><br/>1일 평균 사망자  <br/>819명</button></li>
						</ul>
					  <input type="hidden" name="prdcd" id="prdcd" value=""/>
					  <div class="inp_wrap name_wrap">
							<span class="input_lb"><label for="inquireName">이름</label></span>
							<em class="input_area"><input type="text" class="g_input_01" name="custNm" id="custNm" maxlength="6" placeholder="" onChange={this.handleValueChange} /></em>
						</div>
					  
					  <div class="inp_wrap">
							<span class="input_lb"><label for="inquireBirthday">생년월일</label></span>
							<em class="input_area"><input type="number" class="g_input_01" name="aclBirdt" id="aclBirdt" maxlength="8" placeholder="예) 19940101" onChange={this.handleValueChange} /></em>
						</div>
					  
						<div class="rd_wrap gender_wrap">
							<span class="input_lb">성별</span>
							<em class="input_area">
								<button type="button" className={"gender male "}   value="1" onClick={this.handleGender}>남자</button>
								<button type="button" className={"gender female "} value="2" onClick={this.handleGender}>여자</button>
							</em>
						</div>
						
						<div class="inp_wrap">
							<span class="input_lb"><label for="inquireTel">휴대폰</label></span>
							<em class="input_area"><input type="number" class="g_input_01" name="phoneNo" id="phoneNo" maxlength="11" placeholder="예) 01012345678" onChange={this.handleValueChange}/></em>
						</div>
					</div>
				</div>
			
			</div>
			  <div class="confirm">
				<a href=""><span  class="btn_oneclick01 btnOneclick" onClick={this.handleFormSubmit}>보험료 확인하기</span></a>
			  </div>
			  
			  <div id="premiumJoin" class="onlineJoin" style={this.state.joinStyle}>
			    <CloseIcon color="primary" onClick={ ()=>{ this.setState({joinStyle: {display: 'none'}})} } />
				<div class="conL">
					<dl> <dt>내 월 보험료는</dt></dl>
					<div class="price" id="divPremium"><span id="spnPremium">{this.state.data.prm}</span>
					</div>
				</div>
				<div class="conR">
				<a href=""><Link to={`/joins`}>온라인으로 직접 가입하기</Link></a>
				</div>
			  </div>
			  
		</div>
	</div>
    );
    
  }

}

export default Intro;