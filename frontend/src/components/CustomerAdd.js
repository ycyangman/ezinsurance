import React from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';
import { withStyles } from '@mui/styles';

import ComboBox from '../common/ComboBox';
import ApiService from "../common/ApiService";

const gndrCdList = [
    {cdVl:'1',cdNm:'남자'},
    {cdVl:'2',cdNm:'여자'}
  ]

  const insJobCdList = [
    {cdVl:'000001',cdNm:'무직'},
    {cdVl:'000002',cdNm:'회사원'},
    {cdVl:'000003',cdNm:'공무원'}
  ]

const styles = theme => ({
   formControl: {
    //margin: theme.spacing(0),
    minWidth: 250,
  },
  
    hidden: {
        display: 'none'
    }
});

class CustomerAdd extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            custNm: '',
            aclBirdt: '',
            gndrCd: '',
            insJobCd: '',
            insJobNm: '',
            open: false,
        }
    }


    handleFormSubmit = (e) => {
        e.preventDefault()
		console.log(this.state);
		
        this.addCustomer()
            .then((response) => {
                console.log(response.data);

                //alert(response.data.data.custNo);
                window.localStorage.setItem("custNo", response.data.data.custNo);

                this.props.stateRefresh();
            })
        this.setState({
            custNm: '',
            aclBirdt: '',
            gndrCd: '',
            insJobNm: '',
            open: false
        })
    }

    /*
    handleFileChange = (e) => {
        this.setState({
            file: e.target.files[0],
            fileName: e.target.value
        })
    }
    */

    handleValueChange = (e) => {
		let nextState = {};
        nextState[e.target.name] = e.target.value;
        this.setState(nextState);

        if( e.target.name === 'insJobCd') {

            const jobCd = insJobCdList.find((c) => {
              return e.target.value === c.cdVl;
            })

            if(jobCd) {
                this.setState({insJobNm : jobCd.cdNm})
            }

        }

    }

    addCustomer = () => {
        
        let data = {
    
            svcId : 'CSA001SVC',
            svcFn : 'saveCustomer',
        
            custNm: this.state.custNm,
            aclBirdt: this.state.aclBirdt,
            gndrCd: this.state.gndrCd,
            insJobCd: this.state.insJobCd,
            insJobNm: this.state.insJobNm,
          
        }
        
        console.log(data);

        return ApiService.postTran('/customers/online', data);

    }

    handleClickOpen = () => {
        this.setState({
            open: true
        });
    }

    handleClose = () => {
        this.setState({
            file: null,
            custNm: '',
            aclBirdt: '',
            gndrCd: '',
            insJobNm: '',
            fileName: '',
            open: false
        })
    }

    render() {
        const { classes } = this.props;


        return (
            <div>
                <Button variant="contained" color="primary" onClick={this.handleClickOpen}>
                    고객 추가하기
                </Button>
                <Dialog open={this.state.open} onClose={this.handleClose}>
                    <DialogTitle>고객등록</DialogTitle>
                    <DialogContent>
                        <TextField label="이름" type="text" name="custNm" value={this.state.custNm} onChange={this.handleValueChange}/><br/>
                        <TextField label="생년월일" type="text" name="aclBirdt" value={this.state.aclBirdt} onChange={this.handleValueChange}/><br/>
                        <FormControl className={classes.formControl}>
						<InputLabel id="gender_label" labelWidth={10}>성별</InputLabel><br/>
						<ComboBox name="gndrCd" dataList={gndrCdList} setComboBoxVal={this.handleValueChange} />
						</FormControl><br/>
                        <FormControl className={classes.formControl}>
                        <InputLabel id="insJob_label" labelWidth={10}>직업</InputLabel><br/>
                        <ComboBox name="insJobCd" dataList={insJobCdList} setComboBoxVal={this.handleValueChange} />
                        </FormControl><br/>
                    </DialogContent>
                    <DialogActions>
                        <Button variant="contained" color="primary" onClick={this.handleFormSubmit}>추가</Button>
                        <Button variant="outlined" color="primary" onClick={this.handleClose}>닫기</Button>
                    </DialogActions>
                </Dialog>
            </div>
        )
    }

}

export default withStyles(styles)(CustomerAdd);