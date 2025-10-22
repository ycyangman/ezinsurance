import React, { Component } from 'react';
import { withStyles } from '@mui/styles';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';

const styles = theme => ({
  selectDefault: {
    minWidth: 100,
  }
});


//export default function ComboSelect() {
class ComboBox extends Component {

    constructor(props) {
        super(props);
        this.state = {
        toggle: false,
        value: ''
        };
        }

  handleChange = (event) => {
		this.props.setComboBoxVal(event)
	  
	    this.setState({ value: event.target.value})
  };
render() {
  const { classes } = this.props;
	
  return (
    <div>
        <Select
          name={this.props.name}
		  id="selectGendir"
          value={this.state.value}
          onChange={this.handleChange} className={classes.selectDefault}
        >
		<MenuItem  aria-label="None" value="" />
		  {
            
			this.props.dataList && this.props.dataList.map(data => (
              <MenuItem  key={data.cdVl} value={data.cdVl}>{data.cdNm}</MenuItem >
            ))
          }
        </Select>
    </div>
  );
}
}
export default withStyles(styles)(ComboBox);