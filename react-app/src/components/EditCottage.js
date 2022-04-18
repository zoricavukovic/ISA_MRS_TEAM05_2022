import * as React from 'react';
import { styled } from '@mui/material/styles';
import Card from '@mui/material/Card';
import CardHeader from '@mui/material/CardHeader';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import CardActions from '@mui/material/CardActions';
import Collapse from '@mui/material/Collapse';
import Avatar from '@mui/material/Avatar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import { red } from '@mui/material/colors';
import FavoriteIcon from '@mui/icons-material/Favorite';
import ShareIcon from '@mui/icons-material/Share';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ToggleButton from '@mui/material/ToggleButton';
import ViewModuleIcon from '@mui/icons-material/ViewModule';
import cottageImg from './cottage.jpg';
import Box from '@mui/material/Box';
import { useEffect, useState } from "react";
import EditIcon from '@mui/icons-material/Edit';
import axios from "axios";


const ExpandMore = styled((props) => {
  const { expand, ...other } = props;
  return <IconButton {...other} />;
})(({ theme, expand }) => ({
  transform: !expand ? 'rotate(0deg)' : 'rotate(180deg)',
  marginLeft: 'auto',
  transition: theme.transitions.create('transform', {
    duration: theme.transitions.duration.shortest,
  }),
}));

export default function CardIm() {
  const [expanded, setExpanded] = React.useState(false);
  const [cottageBasicData, setCottageBasicData] = useState({});
  const [isLoading, setLoading] = useState(true);
  const urlPath = "http://localhost:8092/bookingApp/cottages/" + 101;

  const handleExpandClick = () => {
    setExpanded(!expanded);
  };
  const handleMorePictures = (event) => {
    event.preventDefault();
    alert("LA");
  };

  const editCottage = (event) => {
    event.preventDefault();
    alert("Edit");
  };

  useEffect(() => {
        axios.get(urlPath).then(res => {
            setCottageBasicData(res.data);
            setLoading(false);
        })
    }, []);
    if (isLoading) { return <div className="App">Loading...</div> }
  return (
    <Card sx={{  }}>
      <CardMedia
        component="img"
        height="300"
        src={cottageImg}
        alt="Slika vikendice"
      />
      <CardHeader
        
        title={cottageBasicData.name}
        subheader={cottageBasicData.address + ", " + cottageBasicData.place.cityName + ", " + cottageBasicData.place.zipCode + ", " + cottageBasicData.place.stateName}
        action={
            <ToggleButton value="module" aria-label="module onClick={handleMorePictures}">
                <ViewModuleIcon onClick={handleMorePictures}/>
            </ToggleButton>
            
        }
      />
      <CardHeader
        
        action={
            <ToggleButton value="module" aria-label="module" onClick={editCottage}>
                <EditIcon />
            </ToggleButton>
            
        }
      />

    <CardActions disableSpacing>
        <IconButton aria-label="add to favorites">
          <FavoriteIcon />
        </IconButton>
        <IconButton aria-label="share">
          <ShareIcon />
        </IconButton>
        <ExpandMore
          expand={expanded}
          onClick={handleExpandClick}
          aria-expanded={expanded}
          aria-label="show more"
        >
          <ExpandMoreIcon />
        </ExpandMore>
      </CardActions>


      <CardContent>
        <Typography variant="body2" color="text.secondary">
          Ovde ce biti promo description za ovu vikendicu.
        </Typography>
      </CardContent>
      
      <Collapse in={expanded} timeout="auto" unmountOnExit>
        <CardContent>
          <Typography paragraph>Method:</Typography>
          <Typography paragraph>
            Heat 1/2 cup of the broth in a pot until simmering, add saffron and set
            aside for 10 minutes.
          </Typography>
          <Typography paragraph>
            Heat oil in a (14- to 16-inch) paella pan or a large, deep skillet over
            medium-high heat. Add chicken, shrimp and chorizo, and cook, stirring
            occasionally until lightly browned, 6 to 8 minutes. Transfer shrimp to a
            large plate and set aside, leaving chicken and chorizo in the pan. Add
            pimentón, bay leaves, garlic, tomatoes, onion, salt and pepper, and cook,
            stirring often until thickened and fragrant, about 10 minutes. Add
            saffron broth and remaining 4 1/2 cups chicken broth; bring to a boil.
          </Typography>
          <Typography paragraph>
            Add rice and stir very gently to distribute. Top with artichokes and
            peppers, and cook without stirring, until most of the liquid is absorbed,
            15 to 18 minutes. Reduce heat to medium-low, add reserved shrimp and
            mussels, tucking them down into the rice, and cook again without
            stirring, until mussels have opened and rice is just tender, 5 to 7
            minutes more. (Discard any mussels that don&apos;t open.)
          </Typography>
          <Typography>
            Set aside off of the heat to let rest for 10 minutes, and then serve.
          </Typography>
        </CardContent>
      </Collapse>
    </Card>
    
  );
}