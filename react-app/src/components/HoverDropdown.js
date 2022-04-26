import React from 'react';
import { withStyles, Theme, makeStyles } from '@material-ui/core/styles';
import Tooltip from '@material-ui/core/Tooltip';
import { MenuItem, IconButton } from '@material-ui/core';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import styles from 'assets/jss/material-dashboard-pro-react/components/tasksStyle.js';

// @ts-ignore
const useStyles = makeStyles(styles);

const LightTooltip = withStyles((theme: Theme) => ({
  tooltip: {
    backgroundColor: theme.palette.common.white,
    color: 'rgba(0, 0, 0, 0.87)',
    boxShadow: theme.shadows[1],
    fontSize: 11,
    padding: 0,
    margin: 4,
  },
}))(Tooltip);

interface IProps {
  menus: {
    action: () => void;
    name: string;
  }[];
}
const HoverDropdown: React.FC<IProps> = ({ menus }) => {
  const classes = useStyles();
  return (
    <div>
      <LightTooltip
        interactive
        title={
          <React.Fragment>
            {menus.map((item) => {
              return <MenuItem onClick={item.action}>{item.name}</MenuItem>;
            })}
          </React.Fragment>
        }
      >
        <IconButton
          aria-label='more'
          aria-controls='long-menu'
          aria-haspopup='true'
          className={classes.tableActionButton}
        >
          <MoreVertIcon />
        </IconButton>
      </LightTooltip>
    </div>
  );
};

export default HoverDropdown;