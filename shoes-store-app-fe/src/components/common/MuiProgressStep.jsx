import { Box, Step, StepLabel, Stepper } from '@mui/material';
import PropTypes from 'prop-types';

export default function HorizontalLinearAlternativeLabelStepper({
	steps = [],
}) {
	return (
		<Box sx={{ width: '100%' }}>
			<Stepper activeStep={1} alternativeLabel>
				{steps.map((label) => (
					<Step key={label}>
						<StepLabel>{label}</StepLabel>
					</Step>
				))}
			</Stepper>
		</Box>
	);
}

HorizontalLinearAlternativeLabelStepper.propTypes = {
	steps: PropTypes.array.isRequired,
};
